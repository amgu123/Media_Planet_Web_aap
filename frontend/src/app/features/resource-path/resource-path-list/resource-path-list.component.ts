import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResourcePath, JobMachine } from '../../../core/models/entity.model';
import { ResourcePathService } from '../../../core/services/resource-path.service';
import { JobMachineService } from '../../../core/services/job-machine.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-resource-path-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './resource-path-list.component.html'
})
export class ResourcePathListComponent implements OnInit {
    private resourcePathService = inject(ResourcePathService);
    private jobMachineService = inject(JobMachineService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    resourcePaths: ResourcePath[] = [];
    jobMachines: JobMachine[] = [];
    loading = false;

    showModal = false;
    isEdit = false;
    currentId: number | null = null;

    resourcePathForm = this.fb.group({
        jobMachineId: ['', [Validators.required]],
        value: ['', [Validators.required]],
        description: [''],
        priority: ['Primary', [Validators.required]],
        status: [true]
    });

    ngOnInit(): void {
        this.loadResourcePaths();
        this.loadJobMachines();
    }

    loadResourcePaths(): void {
        this.loading = true;
        this.resourcePathService.getAll().subscribe({
            next: (data) => {
                this.resourcePaths = data;
                this.loading = false;
            },
            error: () => this.loading = false
        });
    }

    loadJobMachines(): void {
        this.jobMachineService.getActive().subscribe(data => {
            this.jobMachines = data;
        });
    }

    openAddModal(): void {
        this.isEdit = false;
        this.currentId = null;
        this.resourcePathForm.reset({ status: true, priority: 'Primary' });
        this.showModal = true;
    }

    openEditModal(path: ResourcePath): void {
        this.isEdit = true;
        this.currentId = path.id!;
        this.resourcePathForm.patchValue({
            jobMachineId: path.jobMachine.id?.toString(),
            value: path.value,
            description: path.description,
            priority: path.priority || 'Primary',
            status: path.status
        });
        this.showModal = true;
    }

    closeModal(): void {
        this.showModal = false;
    }

    saveResourcePath(): void {
        if (this.resourcePathForm.valid) {
            const formValue = this.resourcePathForm.value;
            const selectedMachine = this.jobMachines.find(m => m.id?.toString() === formValue.jobMachineId);

            const resourcePathData: any = {
                jobMachine: selectedMachine,
                value: formValue.value,
                description: formValue.description,
                priority: formValue.priority,
                status: formValue.status
            };

            if (this.isEdit && this.currentId) {
                this.resourcePathService.update(this.currentId, resourcePathData).subscribe({
                    next: () => {
                        this.toastService.success('Resource Path updated successfully');
                        this.loadResourcePaths();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to update resource path')
                });
            } else {
                this.resourcePathService.create(resourcePathData).subscribe({
                    next: () => {
                        this.toastService.success('Resource Path created successfully');
                        this.loadResourcePaths();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to create resource path')
                });
            }
        }
    }

    deleteResourcePath(id: number): void {
        if (confirm('Are you sure you want to delete this resource path?')) {
            this.resourcePathService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Resource Path deleted successfully');
                    this.loadResourcePaths();
                },
                error: () => this.toastService.error('Failed to delete resource path')
            });
        }
    }
}
