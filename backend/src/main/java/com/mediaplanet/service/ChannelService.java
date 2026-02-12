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
}
