import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { JwtResponse, LoginRequest, User } from '../models/auth.model';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);
    private router = inject(Router);

    private apiUrl = 'http://localhost:9090/api/auth';
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor() {
        const storedUser = localStorage.getItem('currentUser');
        if (storedUser) {
            this.currentUserSubject.next(JSON.parse(storedUser));
        }
    }

    login(credentials: LoginRequest): Observable<JwtResponse> {
        return this.http.post<JwtResponse>(`${this.apiUrl}/login`, credentials).pipe(
            tap(response => {
                const user: User = {
                    id: response.id,
                    username: response.username,
                    email: response.email,
                    role: response.role
                };
                localStorage.setItem('token', response.token);
                localStorage.setItem('currentUser', JSON.stringify(user));
                this.currentUserSubject.next(user);
            })
        );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }
}
