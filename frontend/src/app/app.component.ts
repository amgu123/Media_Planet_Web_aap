import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToasterContainerComponent } from './core/components/toaster-container/toaster-container.component';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, ToasterContainerComponent],
    template: `
        <router-outlet></router-outlet>
        <app-toaster-container></app-toaster-container>
    `
})
export class AppComponent {
    title = 'Media Planet';
}
