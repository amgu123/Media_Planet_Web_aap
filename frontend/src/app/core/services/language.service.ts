import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Language } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class LanguageService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/languages';

    getAll(): Observable<Language[]> {
        return this.http.get<Language[]>(this.apiUrl);
    }

    getById(id: number): Observable<Language> {
        return this.http.get<Language>(`${this.apiUrl}/${id}`);
    }

    getActive(): Observable<Language[]> {
        return this.http.get<Language[]>(`${this.apiUrl}/active`);
    }

    create(language: Language): Observable<Language> {
        return this.http.post<Language>(this.apiUrl, language);
    }

    update(id: number, language: Language): Observable<Language> {
        return this.http.put<Language>(`${this.apiUrl}/${id}`, language);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
