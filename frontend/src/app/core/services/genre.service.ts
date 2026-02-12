import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Genre } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class GenreService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/genres';

    getAll(): Observable<Genre[]> {
        return this.http.get<Genre[]>(this.apiUrl);
    }

    getById(id: number): Observable<Genre> {
        return this.http.get<Genre>(`${this.apiUrl}/${id}`);
    }

    getActive(): Observable<Genre[]> {
        return this.http.get<Genre[]>(`${this.apiUrl}/active`);
    }

    create(genre: Genre): Observable<Genre> {
        return this.http.post<Genre>(this.apiUrl, genre);
    }

    update(id: number, genre: Genre): Observable<Genre> {
        return this.http.put<Genre>(`${this.apiUrl}/${id}`, genre);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
