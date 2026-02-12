import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConfig } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class AppConfigService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/configs';

    getAll(): Observable<AppConfig[]> {
        return this.http.get<AppConfig[]>(this.apiUrl);
    }

    getById(id: number): Observable<AppConfig> {
        return this.http.get<AppConfig>(`${this.apiUrl}/${id}`);
    }

    getByKey(key: string): Observable<AppConfig> {
        return this.http.get<AppConfig>(`${this.apiUrl}/key/${key}`);
    }

    create(config: AppConfig): Observable<AppConfig> {
        return this.http.post<AppConfig>(this.apiUrl, config);
    }

    update(id: number, config: AppConfig): Observable<AppConfig> {
        return this.http.put<AppConfig>(`${this.apiUrl}/${id}`, config);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
