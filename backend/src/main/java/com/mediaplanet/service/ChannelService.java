package com.mediaplanet.service;

import com.mediaplanet.entity.Channel;
import com.mediaplanet.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private com.mediaplanet.worker.WorkerManager workerManager;

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public Channel getChannelById(Long id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found with id: " + id));
    }

    public List<Channel> getActiveChannels() {
        return channelRepository.findByStatus(true);
    }

    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel updateChannel(Long id, Channel channelDetails) {
        Channel channel = getChannelById(id);

        channel.setChannelName(channelDetails.getChannelName());
        channel.setLogo(channelDetails.getLogo());
        channel.setDescription(channelDetails.getDescription());
        channel.setStatus(channelDetails.getStatus());
        channel.setLanguage(channelDetails.getLanguage());
        channel.setMarket(channelDetails.getMarket());
        channel.setGenre(channelDetails.getGenre());
        channel.setJobMachine(channelDetails.getJobMachine());
        channel.setPrimaryPath(channelDetails.getPrimaryPath());
        channel.setSecondaryPath(channelDetails.getSecondaryPath());
        channel.setAdDetection(channelDetails.getAdDetection());
        channel.setNewsDetection(channelDetails.getNewsDetection());
        channel.setOcr(channelDetails.getOcr());

        return channelRepository.save(channel);
    }

    public void deleteChannel(Long id) {
        Channel channel = getChannelById(id);
        channelRepository.delete(channel);
    }

    public Channel updateWorkerStatus(Long id, String type, boolean running) {
        Channel channel = getChannelById(id);
        switch (type.toUpperCase()) {
            case "AD":
                channel.setAdWorkerRunning(running);
                break;
            case "NEWS":
                channel.setNewsWorkerRunning(running);
                break;
            case "OCR":
                channel.setOcrWorkerRunning(running);
                break;
            default:
                throw new IllegalArgumentException("Invalid task type: " + type);
        }
        Channel savedChannel = channelRepository.save(channel);

        if (running) {
            workerManager.startWorker(savedChannel, type.toUpperCase());
        } else {
            workerManager.stopWorker(id, type.toUpperCase());
        }

        return savedChannel;
    }
}
