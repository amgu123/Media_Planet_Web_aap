package com.mediaplanet.service;

import com.mediaplanet.entity.Market;
import com.mediaplanet.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }

    public Market getMarketById(Long id) {
        return marketRepository.findById(id).orElse(null);
    }

    public Market createMarket(Market market) {
        return marketRepository.save(market);
    }

    public Market updateMarket(Long id, Market marketDetails) {
        Market market = getMarketById(id);
        market.setMarketName(marketDetails.getMarketName());
        market.setLogo(marketDetails.getLogo());
        market.setStatus(marketDetails.getStatus());
        return marketRepository.save(market);
    }

    public void deleteMarket(Long id) {
        Market market = getMarketById(id);
        marketRepository.delete(market);
    }

    public List<Market> getActiveMarkets() {
        return marketRepository.findByStatus(true);
    }
}
