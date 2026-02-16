import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { GeneratedContentService } from '../../../core/services/generated-content.service';
import { ChannelService } from '../../../core/services/channel.service';
import { Channel, GeneratedContent } from '../../../core/models/entity.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-content-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './content-list.component.html',
    styleUrls: ['./content-list.component.scss']
})
export class ContentListComponent implements OnInit {
    private fb = inject(FormBuilder);
    private contentService = inject(GeneratedContentService);
    private channelService = inject(ChannelService);
    private toastService = inject(ToastService);

    filterForm: FormGroup = this.fb.group({
        channelId: [''],
        startDate: [''],
        endDate: [''],
        startTime: [''],
        endTime: ['']
    });

    channels: Channel[] = [];
    contents: GeneratedContent[] = [];
    loading = false;
    expandedRows: Set<number> = new Set();
    searchQueries: Map<number, string> = new Map();

    // Pagination
    currentPage = 0;
    pageSize = 12;
    totalElements = 0;
    totalPages = 0;

    ngOnInit(): void {
        this.loadChannels();
        this.fetchContent();
    }

    loadChannels(): void {
        this.channelService.getAll().subscribe(data => this.channels = data);
    }

    fetchContent(): void {
        this.loading = true;
        const filters = {
            ...this.filterForm.value,
            page: this.currentPage,
            size: this.pageSize
        };

        this.contentService.getFiltered(filters).subscribe({
            next: (response) => {
                this.contents = response.content;
                this.totalElements = response.totalElements;
                this.totalPages = response.totalPages;
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toastService.error('Failed to load content');
            }
        });
    }

    onFilter(): void {
        this.currentPage = 0;
        this.fetchContent();
    }

    onPageChange(page: number): void {
        this.currentPage = page;
        this.fetchContent();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    resetFilters(): void {
        this.filterForm.reset({
            channelId: '',
            startDate: '',
            endDate: '',
            startTime: '',
            endTime: ''
        });
        this.currentPage = 0;
        this.fetchContent();
    }

    toggleExpand(id: number | undefined): void {
        if (id === undefined) return;
        if (this.expandedRows.has(id)) {
            this.expandedRows.delete(id);
        } else {
            this.expandedRows.add(id);
        }
    }

    isExpanded(id: number | undefined): boolean {
        return id !== undefined && this.expandedRows.has(id);
    }

    onSearchChange(id: number | undefined, query: string): void {
        if (id === undefined) return;
        this.searchQueries.set(id, query.toLowerCase());
    }

    getFilteredTranscripts(item: GeneratedContent): any[] {
        const query = this.searchQueries.get(item.id!) || '';
        if (!query) return item.transcripts || [];

        return (item.transcripts || []).filter(t =>
            t.text.toLowerCase().includes(query) ||
            t.timeStamp.toLowerCase().includes(query)
        );
    }
}
