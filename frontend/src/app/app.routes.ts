import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: 'login',
        loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
    },
    {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
        canActivate: [authGuard],
        children: [
            {
                path: '',
                redirectTo: 'languages',
                pathMatch: 'full'
            },
            {
                path: 'languages',
                loadComponent: () => import('./features/language/language-list/language-list.component').then(m => m.LanguageListComponent)
            },
            {
                path: 'markets',
                loadComponent: () => import('./features/market/market-list/market-list.component').then(m => m.MarketListComponent)
            },
            {
                path: 'genres',
                loadComponent: () => import('./features/genre/genre-list/genre-list.component').then(m => m.GenreListComponent)
            },
            {
                path: 'job-machines',
                loadComponent: () => import('./features/job-machine/job-machine-list/job-machine-list.component').then(m => m.JobMachineListComponent)
            },
            {
                path: 'resource-paths',
                loadComponent: () => import('./features/resource-path/resource-path-list/resource-path-list.component').then(m => m.ResourcePathListComponent)
            },
            {
                path: 'channels',
                loadComponent: () => import('./features/channel/channel-list/channel-list.component').then(m => m.ChannelListComponent)
            },
            {
                path: 'channels/create',
                loadComponent: () => import('./features/channel/channel-form/channel-form.component').then(m => m.ChannelFormComponent)
            },
            {
                path: 'channels/edit/:id',
                loadComponent: () => import('./features/channel/channel-form/channel-form.component').then(m => m.ChannelFormComponent)
            },
            {
                path: 'task-monitor',
                loadComponent: () => import('./features/task-monitor/task-monitor.component').then(m => m.TaskMonitorComponent)
            },
            {
                path: 'generated-content',
                loadComponent: () => import('./features/generated-content/content-list/content-list.component').then(m => m.ContentListComponent)
            },
            {
                path: 'app-config',
                loadComponent: () => import('./features/app-config/app-config.component').then(m => m.AppConfigComponent)
            }
        ]
    },
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    },
    {
        path: '**',
        redirectTo: '/login'
    }
];
