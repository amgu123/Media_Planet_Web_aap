import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Market } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class MarketService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/markets';

    getAll(): Observable<Market[]> {
        return this.http.get<Market[]>(this.apiUrl);
    }

    getById(id: number): Observable<Market> {
        return this.http.get<Market>(`${this.apiUrl}/${id}`);
    }

    getActive(): Observable<Market[]> {
        return this.http.get<Market[]>(`${this.apiUrl}/active`);
    }

    create(market: Market): Observable<Market> {
        return this.http.post<Market>(this.apiUrl, market);
    }

    update(id: number, market: Market): Observable<Market> {
        return this.http.put<Market>(`${this.apiUrl}/${id}`, market);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
