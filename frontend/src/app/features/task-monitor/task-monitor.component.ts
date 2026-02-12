import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ChannelService } from '../../core/services/channel.service';
import { TaskExecutionService } from '../../core/services/task-execution.service';
import { Channel, TaskExecution } from '../../core/models/entity.model';
import { ToastService } from '../../core/services/toast.service';

@Component({
    selector: 'app-task-monitor',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './task-monitor.component.html',
    styleUrls: ['./task-monitor.component.scss']
})
export class TaskMonitorComponent implements OnInit {
    private channelService = inject(ChannelService);
    private taskService = inject(TaskExecutionService);
    private toastService = inject(ToastService);

    channels: Channel[] = [];
    taskStatuses: Map<string, TaskExecution> = new Map(); // key: channelId-taskType
    loading = false;
    logs: string = '';
    selectedTaskInfo: { channelName: string, taskType: string } | null = null;
    showLogModal = false;

    ngOnInit(): void {
        this.loadChannels();
    }

    loadChannels(): void {
        this.loading = true;
        this.channelService.getAll().subscribe({
            next: (data) => {
                this.channels = data;
                this.loadAllTaskStatuses();
            },
            error: () => {
                this.loading = false;
                this.toastService.error('Failed to load channels');
            }
        });
    }

    loadAllTaskStatuses(): void {
        this.taskService.getAll().subscribe({
            next: (executions) => {
                executions.forEach(exec => {
                    const key = `${exec.channel.id}-${exec.taskType}`;
                    this.taskStatuses.set(key, exec);
                });
                this.loading = false;
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    getTaskStatus(channelId: number, taskType: string): string {
        const key = `${channelId}-${taskType}`;
        return this.taskStatuses.get(key)?.status || 'STOPPED';
    }

    startTask(channel: Channel, taskType: string): void {
        this.taskService.startTask(channel.id!, taskType).subscribe({
            next: (exec) => {
                const key = `${channel.id}-${taskType}`;
                this.taskStatuses.set(key, exec);
                this.toastService.success(`${taskType} started for ${channel.channelName}`);
            },
            error: () => this.toastService.error('Failed to start task')
        });
    }

    stopTask(channel: Channel, taskType: string): void {
        this.taskService.stopTask(channel.id!, taskType).subscribe({
            next: (exec) => {
                const key = `${channel.id}-${taskType}`;
                this.taskStatuses.set(key, exec);
                this.toastService.success(`${taskType} stopped for ${channel.channelName}`);
            },
            error: () => this.toastService.error('Failed to stop task')
        });
    }

    viewLogs(channel: Channel, taskType: string): void {
        this.selectedTaskInfo = { channelName: channel.channelName, taskType };
        this.taskService.getLogs(channel.id!, taskType).subscribe({
            next: (data) => {
                this.logs = data.logs;
                this.showLogModal = true;
            },
            error: () => this.toastService.error('Failed to fetch logs')
        });
    }

    closeLogModal(): void {
        this.showLogModal = false;
        this.logs = '';
        this.selectedTaskInfo = null;
    }
}
