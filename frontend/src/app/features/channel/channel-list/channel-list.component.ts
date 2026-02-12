import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Channel } from '../../../core/models/entity.model';
import { ChannelService } from '../../../core/services/channel.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
    selector: 'app-channel-list',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './channel-list.component.html',
    styleUrls: ['./channel-list.component.scss']
})
export class ChannelListComponent implements OnInit {
    private channelService = inject(ChannelService);
    private toastService = inject(ToastService);

    channels: Channel[] = [];
    loading = false;

    ngOnInit(): void {
        this.loadChannels();
    }

    loadChannels(): void {
        this.loading = true;
        this.channelService.getAll().subscribe({
            next: (data) => {
                this.channels = data;
                this.loading = false;
            },
            error: () => {
                this.loading = false;
                this.toastService.error('Failed to load channels');
            }
        });
    }

    deleteChannel(id: number): void {
        if (confirm('Are you sure you want to delete this channel?')) {
            this.channelService.delete(id).subscribe({
                next: () => {
                    this.toastService.success('Channel deleted successfully');
                    this.loadChannels();
                },
                error: () => this.toastService.error('Failed to delete channel')
            });
        }
    }
}
