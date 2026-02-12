import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
    private authService = inject(AuthService);
    currentUser$ = this.authService.currentUser$;
    userInitial$ = this.currentUser$.pipe(
        map(user => user?.username?.substring(0, 1)?.toUpperCase() || 'U')
    );
    isSidebarCollapsed = false;

    logout(): void {
        this.authService.logout();
    }

    toggleSidebar(): void {
        this.isSidebarCollapsed = !this.isSidebarCollapsed;
    }
}
