package com.mediaplanet.service;

import com.mediaplanet.entity.GeneratedContent;
import com.mediaplanet.repository.GeneratedContentRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratedContentService {

    @Autowired
    private GeneratedContentRepository repository;

    public Page<GeneratedContent> getFilteredContent(
            Long channelId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            LocalTime startTime,
            LocalTime endTime,
            Pageable pageable) {

        if (pageable == null) {
            pageable = Pageable.unpaged();
        }

        return repository.findAll((Specification<GeneratedContent>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (channelId != null) {
                predicates.add(cb.equal(root.get("channel").get("id"), channelId));
            }

            if (startDateTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), startDateTime));
            }

            if (endDateTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), endDateTime));
            }

            // Time filters are a bit more complex since we need to extract time from
            // timestamp
            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        cb.function("TIME", LocalTime.class, root.get("timestamp")),
                        startTime));
            }

            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        cb.function("TIME", LocalTime.class, root.get("timestamp")),
                        endTime));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    public GeneratedContent saveContent(GeneratedContent content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        return repository.save(content);
    }
}
