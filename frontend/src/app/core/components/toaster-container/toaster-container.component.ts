import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService, Toast } from '../../services/toast.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'app-toaster-container',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './toaster-container.component.html',
    styleUrls: ['./toaster-container.component.scss']
})
export class ToasterContainerComponent implements OnInit {
    toasts$: Observable<Toast[]>;

    constructor(private toastService: ToastService) {
        this.toasts$ = this.toastService.toasts$;
    }

    ngOnInit(): void { }

    removeToast(id: number) {
        this.toastService.remove(id);
    }
}
