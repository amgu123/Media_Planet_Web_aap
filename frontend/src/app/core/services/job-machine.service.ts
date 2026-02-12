import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobMachine } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class JobMachineService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/job-machines';

    getAll(): Observable<JobMachine[]> {
        return this.http.get<JobMachine[]>(this.apiUrl);
    }

    getById(id: number): Observable<JobMachine> {
        return this.http.get<JobMachine>(`${this.apiUrl}/${id}`);
    }

    getActive(): Observable<JobMachine[]> {
        return this.http.get<JobMachine[]>(`${this.apiUrl}/active`);
    }

    create(jobMachine: JobMachine): Observable<JobMachine> {
        return this.http.post<JobMachine>(this.apiUrl, jobMachine);
    }

    update(id: number, jobMachine: JobMachine): Observable<JobMachine> {
        return this.http.put<JobMachine>(`${this.apiUrl}/${id}`, jobMachine);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
