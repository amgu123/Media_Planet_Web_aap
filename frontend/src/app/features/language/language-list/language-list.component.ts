import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Language } from '../../../core/models/entity.model';
import { LanguageService } from '../../../core/services/language.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-language-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './language-list.component.html'
})
export class LanguageListComponent implements OnInit {
    private languageService = inject(LanguageService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    languages: Language[] = [];
    loading = false;

    // Modal state
    showModal = false;
    isEdit = false;
    currentId: number | null = null;

    languageForm = this.fb.group({
        languageName: ['', [Validators.required]],
        logo: ['', [Validators.required]],
        status: [true]
    });

    ngOnInit(): void {
        this.loadLanguages();
    }

    loadLanguages(): void {
        this.loading = true;
        this.languageService.getAll().subscribe({
            next: (data) => {
                this.languages = data;
                this.loading = false;
            },
            error: () => this.loading = false
        });
    }

    openAddModal(): void {
        this.isEdit = false;
        this.currentId = null;
        this.languageForm.reset({ status: true });
        this.showModal = true;
    }

    openEditModal(language: Language): void {
        this.isEdit = true;
        this.currentId = language.id!;
        this.languageForm.patchValue({
            languageName: language.languageName,
            logo: language.logo,
            status: language.status
        });
        this.showModal = true;
    }

    closeModal(): void {
        this.showModal = false;
    }

    saveLanguage(): void {
        if (this.languageForm.valid) {
            const languageData = this.languageForm.value as Language;

            if (this.isEdit && this.currentId) {
                this.languageService.update(this.currentId, languageData).subscribe({
                    next: () => {
                        this.toastService.success('Language updated successfully');
                        this.loadLanguages();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to update language')
                });
            } else {
                this.languageService.create(languageData).subscribe({
                    next: () => {
                        this.toastService.success('Language created successfully');
                        this.loadLanguages();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to create language')
                });
            }
        }
    }

    deleteLanguage(id: number): void {
        if (confirm('Are you sure you want to delete this language?')) {
            this.languageService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Language deleted successfully');
                    this.loadLanguages();
                },
                error: () => this.toastService.error('Failed to delete language')
            });
        }
    }
}
