import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResourcePath } from '../models/entity.model';

@Injectable({
    providedIn: 'root'
})
export class ResourcePathService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:9090/api/resource-paths';

    getAll(): Observable<ResourcePath[]> {
        return this.http.get<ResourcePath[]>(this.apiUrl);
    }

    getById(id: number): Observable<ResourcePath> {
        return this.http.get<ResourcePath>(`${this.apiUrl}/${id}`);
    }

    getByJobMachine(machineId: number): Observable<ResourcePath[]> {
        return this.http.get<ResourcePath[]>(`${this.apiUrl}/job-machine/${machineId}`);
    }

    create(resourcePath: ResourcePath): Observable<ResourcePath> {
        return this.http.post<ResourcePath>(this.apiUrl, resourcePath);
    }

    update(id: number, resourcePath: ResourcePath): Observable<ResourcePath> {
        return this.http.put<ResourcePath>(`${this.apiUrl}/${id}`, resourcePath);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
