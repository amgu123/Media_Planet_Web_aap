import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
    id: number;
    message: string;
    type: 'success' | 'error' | 'info' | 'warning';
    title?: string;
}

@Injectable({
    providedIn: 'root'
})
export class ToastService {
    private toasts: Toast[] = [];
    private toastsSubject = new BehaviorSubject<Toast[]>([]);
    public toasts$ = this.toastsSubject.asObservable();
    private nextId = 0;

    show(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info', title?: string) {
        const toast: Toast = {
            id: this.nextId++,
            message,
            type,
            title
        };
        this.toasts.push(toast);
        this.toastsSubject.next([...this.toasts]);

        // Auto remove after 5 seconds
        setTimeout(() => {
            this.remove(toast.id);
        }, 5000);
    }

    success(message: string, title: string = 'Success') {
        this.show(message, 'success', title);
    }

    error(message: string, title: string = 'Error') {
        this.show(message, 'error', title);
    }

    info(message: string, title: string = 'Info') {
        this.show(message, 'info', title);
    }

    warning(message: string, title: string = 'Warning') {
        this.show(message, 'warning', title);
    }

    remove(id: number) {
        this.toasts = this.toasts.filter(t => t.id !== id);
        this.toastsSubject.next([...this.toasts]);
    }
}
