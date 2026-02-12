import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private router = inject(Router);
    private toastService = inject(ToastService);

    loginForm = this.fb.group({
        username: ['', [Validators.required]],
        password: ['', [Validators.required]]
    });

    error: string | null = null;
    loading = false;

    onSubmit(): void {
        if (this.loginForm.valid) {
            this.loading = true;
            this.error = null;

            const credentials = {
                username: this.loginForm.get('username')?.value ?? '',
                password: this.loginForm.get('password')?.value ?? ''
            };

            this.authService.login(credentials).subscribe({
                next: () => {
                    this.toastService.success('Logged in successfully', 'Welcome!');
                    this.router.navigate(['/dashboard']);
                },
                error: (err) => {
                    this.error = 'Invalid username or password';
                    this.toastService.error(this.error, 'Login Failed');
                    this.loading = false;
                }
            });
        }
    }
}
