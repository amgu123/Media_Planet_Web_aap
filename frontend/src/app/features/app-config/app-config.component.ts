import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AppConfigService } from '../../core/services/app-config.service';
import { AppConfig } from '../../core/models/entity.model';
import { ToastService } from '../../core/services/toast.service';

@Component({
    selector: 'app-app-config',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './app-config.component.html',
    styleUrls: ['./app-config.component.scss']
})
export class AppConfigComponent implements OnInit {
    private configService = inject(AppConfigService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    configs: AppConfig[] = [];
    configForm: FormGroup;
    showModal = false;
    editingConfigId: number | null = null;

    constructor() {
        this.configForm = this.fb.group({
            configKey: ['', Validators.required],
            configValue: ['', Validators.required],
            description: ['']
        });
    }

    ngOnInit(): void {
        this.loadConfigs();
    }

    loadConfigs(): void {
        this.configService.getAll().subscribe({
            next: (data) => this.configs = data,
            error: () => this.toastService.error('Failed to load configurations')
        });
    }

    openModal(): void {
        this.showModal = true;
        this.editingConfigId = null;
        this.configForm.reset();
    }

    closeModal(): void {
        this.showModal = false;
        this.editingConfigId = null;
        this.configForm.reset();
    }

    editConfig(config: AppConfig): void {
        this.editingConfigId = config.id!;
        this.configForm.patchValue({
            configKey: config.configKey,
            configValue: config.configValue,
            description: config.description
        });
        this.showModal = true;
    }

    saveConfig(): void {
        if (this.configForm.invalid) return;

        const configData: AppConfig = this.configForm.value;

        if (this.editingConfigId) {
            this.configService.update(this.editingConfigId, configData).subscribe({
                next: () => {
                    this.toastService.success('Configuration updated');
                    this.loadConfigs();
                    this.closeModal();
                },
                error: () => this.toastService.error('Failed to update configuration')
            });
        } else {
            this.configService.create(configData).subscribe({
                next: () => {
                    this.toastService.success('Configuration created');
                    this.loadConfigs();
                    this.closeModal();
                },
                error: (err: any) => {
                    if (err.status === 409 || (err.error && err.error.message && err.error.message.includes('Duplicate'))) {
                        this.toastService.error('Config key already exists');
                    } else {
                        this.toastService.error('Failed to create configuration');
                    }
                }
            });
        }
    }

    deleteConfig(id: number): void {
        if (confirm('Are you sure you want to delete this configuration?')) {
            this.configService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Configuration deleted');
                    this.loadConfigs();
                },
                error: () => this.toastService.error('Failed to delete configuration')
            });
        }
    }
}
