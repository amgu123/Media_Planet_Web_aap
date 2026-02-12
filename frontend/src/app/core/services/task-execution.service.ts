import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskExecution } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class TaskExecutionService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/task-executions';

    getAll(): Observable<TaskExecution[]> {
        return this.http.get<TaskExecution[]>(this.apiUrl);
    }

    getByChannel(channelId: number): Observable<TaskExecution[]> {
        return this.http.get<TaskExecution[]>(`${this.apiUrl}/channel/${channelId}`);
    }

    startTask(channelId: number, taskType: string): Observable<TaskExecution> {
        return this.http.post<TaskExecution>(`${this.apiUrl}/${channelId}/${taskType}/start`, {});
    }

    stopTask(channelId: number, taskType: string): Observable<TaskExecution> {
        return this.http.post<TaskExecution>(`${this.apiUrl}/${channelId}/${taskType}/stop`, {});
    }

    getLogs(channelId: number, taskType: string): Observable<{ logs: string }> {
        return this.http.get<{ logs: string }>(`${this.apiUrl}/${channelId}/${taskType}/logs`);
    }
}
