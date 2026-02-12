import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Genre } from '../../../core/models/entity.model';
import { GenreService } from '../../../core/services/genre.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-genre-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './genre-list.component.html'
})
export class GenreListComponent implements OnInit {
    private genreService = inject(GenreService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    genres: Genre[] = [];
    loading = false;

    showModal = false;
    isEdit = false;
    currentId: number | null = null;

    genreForm = this.fb.group({
        genreName: ['', [Validators.required]],
        logo: ['', [Validators.required]],
        status: [true]
    });

    ngOnInit(): void {
        this.loadGenres();
    }

    loadGenres(): void {
        this.loading = true;
        this.genreService.getAll().subscribe({
            next: (data) => {
                this.genres = data;
                this.loading = false;
            },
            error: () => this.loading = false
        });
    }

    openAddModal(): void {
        this.isEdit = false;
        this.currentId = null;
        this.genreForm.reset({ status: true });
        this.showModal = true;
    }

    openEditModal(genre: Genre): void {
        this.isEdit = true;
        this.currentId = genre.id!;
        this.genreForm.patchValue({
            genreName: genre.genreName,
            logo: genre.logo,
            status: genre.status
        });
        this.showModal = true;
    }

    closeModal(): void {
        this.showModal = false;
    }

    saveGenre(): void {
        if (this.genreForm.valid) {
            const genreData = this.genreForm.value as Genre;

            if (this.isEdit && this.currentId) {
                this.genreService.update(this.currentId, genreData).subscribe({
                    next: () => {
                        this.toastService.success('Genre updated successfully');
                        this.loadGenres();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to update genre')
                });
            } else {
                this.genreService.create(genreData).subscribe({
                    next: () => {
                        this.toastService.success('Genre created successfully');
                        this.loadGenres();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to create genre')
                });
            }
        }
    }

    deleteGenre(id: number): void {
        if (confirm('Are you sure you want to delete this genre?')) {
            this.genreService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Genre deleted successfully');
                    this.loadGenres();
                },
                error: () => this.toastService.error('Failed to delete genre')
            });
        }
    }
}
