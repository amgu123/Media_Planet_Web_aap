package com.mediaplanet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "markets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "logo", length = 500)
    private String logo;

    @Column(name = "market_name", nullable = false, length = 100)
    private String marketName;

    @Column(nullable = false)
    private Boolean status = true;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
