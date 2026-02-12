import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Channel } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class ChannelService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/channels';

    getAll(): Observable<Channel[]> {
        return this.http.get<Channel[]>(this.apiUrl);
    }

    getById(id: number): Observable<Channel> {
        return this.http.get<Channel>(`${this.apiUrl}/${id}`);
    }

    getActive(): Observable<Channel[]> {
        return this.http.get<Channel[]>(`${this.apiUrl}/active`);
    }

    create(channel: Channel): Observable<Channel> {
        return this.http.post<Channel>(this.apiUrl, channel);
    }

    update(id: number, channel: Channel): Observable<Channel> {
        return this.http.put<Channel>(`${this.apiUrl}/${id}`, channel);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    updateWorkerStatus(id: number, type: string, running: boolean): Observable<Channel> {
        return this.http.post<Channel>(`${this.apiUrl}/${id}/workers/status?type=${type}&running=${running}`, {});
    }
}
