import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobMachine } from '../../../core/models/entity.model';
import { JobMachineService } from '../../../core/services/job-machine.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-job-machine-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './job-machine-list.component.html'
})
export class JobMachineListComponent implements OnInit {
    private jobMachineService = inject(JobMachineService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    jobMachines: JobMachine[] = [];
    loading = false;

    showModal = false;
    isEdit = false;
    currentId: number | null = null;

    jobMachineForm = this.fb.group({
        machineName: ['', [Validators.required]],
        value: ['', [Validators.required]],
        description: [''],
        status: [true]
    });

    ngOnInit(): void {
        this.loadJobMachines();
    }

    loadJobMachines(): void {
        this.loading = true;
        this.jobMachineService.getAll().subscribe({
            next: (data) => {
                this.jobMachines = data;
                this.loading = false;
            },
            error: () => this.loading = false
        });
    }

    openAddModal(): void {
        this.isEdit = false;
        this.currentId = null;
        this.jobMachineForm.reset({ status: true });
        this.showModal = true;
    }

    openEditModal(jobMachine: JobMachine): void {
        this.isEdit = true;
        this.currentId = jobMachine.id!;
        this.jobMachineForm.patchValue({
            machineName: jobMachine.machineName,
            value: jobMachine.value,
            description: jobMachine.description,
            status: jobMachine.status
        });
        this.showModal = true;
    }

    closeModal(): void {
        this.showModal = false;
    }

    saveJobMachine(): void {
        if (this.jobMachineForm.valid) {
            const jobMachineData = this.jobMachineForm.value as JobMachine;

            if (this.isEdit && this.currentId) {
                this.jobMachineService.update(this.currentId, jobMachineData).subscribe({
                    next: () => {
                        this.toastService.success('Job Machine updated successfully');
                        this.loadJobMachines();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to update job machine')
                });
            } else {
                this.jobMachineService.create(jobMachineData).subscribe({
                    next: () => {
                        this.toastService.success('Job Machine created successfully');
                        this.loadJobMachines();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to create job machine')
                });
            }
        }
    }

    deleteJobMachine(id: number): void {
        if (confirm('Are you sure you want to delete this job machine?')) {
            this.jobMachineService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Job Machine deleted successfully');
                    this.loadJobMachines();
                },
                error: () => this.toastService.error('Failed to delete job machine')
            });
        }
    }
}
