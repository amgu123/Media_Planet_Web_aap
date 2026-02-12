package com.mediaplanet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "channels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_name", nullable = false, length = 100)
    private String channelName;

    @Lob
    @Column(name = "logo", columnDefinition = "LONGTEXT")
    private String logo;

    @Column(name = "description", length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "job_machine_id")
    private JobMachine jobMachine;

    @ManyToOne
    @JoinColumn(name = "primary_path_id")
    private ResourcePath primaryPath;

    @ManyToOne
    @JoinColumn(name = "secondary_path_id")
    private ResourcePath secondaryPath;

    @Column(name = "ad_detection")
    private Boolean adDetection = false;

    @Column(name = "news_detection")
    private Boolean newsDetection = false;

    @Column(name = "ocr")
    private Boolean ocr = false;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
