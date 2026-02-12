import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Market } from '../../../core/models/entity.model';
import { MarketService } from '../../../core/services/market.service';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-market-list',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './market-list.component.html'
})
export class MarketListComponent implements OnInit {
    private marketService = inject(MarketService);
    private fb = inject(FormBuilder);
    private toastService = inject(ToastService);

    markets: Market[] = [];
    loading = false;

    showModal = false;
    isEdit = false;
    currentId: number | null = null;

    marketForm = this.fb.group({
        marketName: ['', [Validators.required]],
        logo: ['', [Validators.required]],
        status: [true]
    });

    ngOnInit(): void {
        this.loadMarkets();
    }

    loadMarkets(): void {
        this.loading = true;
        this.marketService.getAll().subscribe({
            next: (data) => {
                this.markets = data;
                this.loading = false;
            },
            error: () => this.loading = false
        });
    }

    openAddModal(): void {
        this.isEdit = false;
        this.currentId = null;
        this.marketForm.reset({ status: true });
        this.showModal = true;
    }

    openEditModal(market: Market): void {
        this.isEdit = true;
        this.currentId = market.id!;
        this.marketForm.patchValue({
            marketName: market.marketName,
            logo: market.logo,
            status: market.status
        });
        this.showModal = true;
    }

    closeModal(): void {
        this.showModal = false;
    }

    saveMarket(): void {
        if (this.marketForm.valid) {
            const marketData = this.marketForm.value as Market;

            if (this.isEdit && this.currentId) {
                this.marketService.update(this.currentId, marketData).subscribe({
                    next: () => {
                        this.toastService.success('Market updated successfully');
                        this.loadMarkets();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to update market')
                });
            } else {
                this.marketService.create(marketData).subscribe({
                    next: () => {
                        this.toastService.success('Market created successfully');
                        this.loadMarkets();
                        this.closeModal();
                    },
                    error: () => this.toastService.error('Failed to create market')
                });
            }
        }
    }

    deleteMarket(id: number): void {
        if (confirm('Are you sure you want to delete this market?')) {
            this.marketService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Market deleted successfully');
                    this.loadMarkets();
                },
                error: () => this.toastService.error('Failed to delete market')
            });
        }
    }
}
