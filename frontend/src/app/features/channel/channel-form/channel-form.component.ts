import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Channel, Language, Market, Genre, JobMachine, ResourcePath } from '../../../core/models/entity.model';
import { ChannelService } from '../../../core/services/channel.service';
import { LanguageService } from '../../../core/services/language.service';
import { MarketService } from '../../../core/services/market.service';
import { GenreService } from '../../../core/services/genre.service';
import { JobMachineService } from '../../../core/services/job-machine.service';
import { ResourcePathService } from '../../../core/services/resource-path.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-channel-form',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
    templateUrl: './channel-form.component.html',
    styleUrls: ['./channel-form.component.scss']
})
export class ChannelFormComponent implements OnInit {
    private fb = inject(FormBuilder);
    private channelService = inject(ChannelService);
    private languageService = inject(LanguageService);
    private marketService = inject(MarketService);
    private genreService = inject(GenreService);
    private jobMachineService = inject(JobMachineService);
    private resourcePathService = inject(ResourcePathService);
    private toastService = inject(ToastService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);

    channelForm: FormGroup;
    isEdit = false;
    currentId: number | null = null;
    loading = false;
    saving = false;

    languages: Language[] = [];
    markets: Market[] = [];
    genres: Genre[] = [];
    jobMachines: JobMachine[] = [];
    resourcePaths: ResourcePath[] = [];
    logoPreview: string | null = null;

    constructor() {
        this.channelForm = this.fb.group({
            channelName: ['', [Validators.required]],
            logo: [''],
            description: [''],
            status: [true],
            language: [null, [Validators.required]],
            market: [null, [Validators.required]],
            genre: [null, [Validators.required]],
            jobMachine: [null, [Validators.required]],
            primaryPath: [null],
            secondaryPath: [null],
            adDetection: [false],
            newsDetection: [false],
            ocr: [false]
        });

        // Listen for Job Machine changes to reload paths
        this.channelForm.get('jobMachine')?.valueChanges.subscribe(machineId => {
            if (machineId) {
                this.loadResourcePaths(machineId);
            } else {
                this.resourcePaths = [];
            }
        });
    }

    ngOnInit(): void {
        this.loadDropdownData();
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEdit = true;
            this.currentId = +id;
            this.loadChannel(this.currentId);
        }
    }

    loadDropdownData(): void {
        this.languageService.getActive().subscribe(data => this.languages = data);
        this.marketService.getActive().subscribe(data => this.markets = data);
        this.genreService.getActive().subscribe(data => this.genres = data);
        this.jobMachineService.getActive().subscribe(data => this.jobMachines = data);
    }

    loadResourcePaths(machineId: number): void {
        this.resourcePathService.getByJobMachine(machineId).subscribe(data => {
            this.resourcePaths = data;
        });
    }

    loadChannel(id: number): void {
        this.loading = true;
        this.channelService.getById(id).subscribe({
            next: (channel) => {
                // First patch basic fields and jobMachine to trigger path loading
                this.channelForm.patchValue({
                    channelName: channel.channelName,
                    description: channel.description,
                    status: channel.status,
                    language: channel.language?.id,
                    market: channel.market?.id,
                    genre: channel.genre?.id,
                    jobMachine: channel.jobMachine?.id,
                    adDetection: channel.adDetection || false,
                    newsDetection: channel.newsDetection || false,
                    ocr: channel.ocr || false
                }, { emitEvent: false }); // Prevent dual loading

                this.logoPreview = channel.logo;

                if (channel.jobMachine?.id) {
                    this.resourcePathService.getByJobMachine(channel.jobMachine.id).subscribe(data => {
                        this.resourcePaths = data;
                        // Patch paths only after options are available
                        this.channelForm.patchValue({
                            primaryPath: channel.primaryPath?.id,
                            secondaryPath: channel.secondaryPath?.id
                        });
                        this.loading = false;
                    });
                } else {
                    this.loading = false;
                }
            },
            error: () => {
                this.toastService.error('Failed to load channel details');
                this.router.navigate(['/dashboard/channels']);
            }
        });
    }

    onFileSelected(event: any): void {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = () => {
                this.logoPreview = reader.result as string;
                this.channelForm.patchValue({ logo: this.logoPreview });
            };
            reader.readAsDataURL(file);
        }
    }

    onSubmit(): void {
        if (this.channelForm.valid) {
            this.saving = true;
            const formValue = this.channelForm.value;

            const channelData: any = {
                ...formValue,
                language: { id: formValue.language },
                market: { id: formValue.market },
                genre: { id: formValue.genre },
                jobMachine: { id: formValue.jobMachine },
                primaryPath: formValue.primaryPath ? { id: formValue.primaryPath } : null,
                secondaryPath: formValue.secondaryPath ? { id: formValue.secondaryPath } : null,
                adDetection: formValue.adDetection,
                newsDetection: formValue.newsDetection,
                ocr: formValue.ocr
            };

            if (this.isEdit && this.currentId) {
                this.channelService.update(this.currentId, channelData).subscribe({
                    next: () => {
                        this.toastService.success('Channel updated successfully');
                        this.router.navigate(['/dashboard/channels']);
                    },
                    error: () => {
                        this.toastService.error('Failed to update channel');
                        this.saving = false;
                    }
                });
            } else {
                this.channelService.create(channelData).subscribe({
                    next: () => {
                        this.toastService.success('Channel created successfully');
                        this.router.navigate(['/dashboard/channels']);
                    },
                    error: () => {
                        this.toastService.error('Failed to create channel');
                        this.saving = false;
                    }
                });
            }
        }
    }
}
