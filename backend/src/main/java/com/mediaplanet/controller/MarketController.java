package com.mediaplanet.controller;

import com.mediaplanet.entity.Market;
import com.mediaplanet.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/markets")
@CrossOrigin(origins = "*")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping
    public ResponseEntity<List<Market>> getAllMarkets() {
        return ResponseEntity.ok(marketService.getAllMarkets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Market> getMarketById(@PathVariable Long id) {
        return ResponseEntity.ok(marketService.getMarketById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Market>> getActiveMarkets() {
        return ResponseEntity.ok(marketService.getActiveMarkets());
    }

    @PostMapping
    public ResponseEntity<Market> createMarket(@RequestBody Market market) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketService.createMarket(market));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Market> updateMarket(@PathVariable Long id, @RequestBody Market market) {
        return ResponseEntity.ok(marketService.updateMarket(id, market));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarket(@PathVariable Long id) {
        marketService.deleteMarket(id);
        return ResponseEntity.noContent().build();
    }
}
