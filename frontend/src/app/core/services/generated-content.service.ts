import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GeneratedContent } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class GeneratedContentService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/generated-content';

    getFiltered(filters: {
        channelId?: number,
        startDate?: string,
        endDate?: string,
        startTime?: string,
        endTime?: string,
        page?: number,
        size?: number
    }): Observable<any> {
        let params = new HttpParams();

        if (filters.channelId) params = params.set('channelId', filters.channelId.toString());
        if (filters.startDate) params = params.set('startDate', filters.startDate);
        if (filters.endDate) params = params.set('endDate', filters.endDate);
        if (filters.startTime) params = params.set('startTime', filters.startTime);
        if (filters.endTime) params = params.set('endTime', filters.endTime);
        if (filters.page !== undefined) params = params.set('page', filters.page.toString());
        if (filters.size !== undefined) params = params.set('size', filters.size.toString());

        return this.http.get<any>(this.apiUrl, { params });
    }
}
