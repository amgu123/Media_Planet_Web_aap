package com.mediaplanet.repository;

import com.mediaplanet.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByStatus(Boolean status);

    List<Market> findByMarketNameContainingIgnoreCase(String name);
}
