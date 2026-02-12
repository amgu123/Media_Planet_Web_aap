package com.mediaplanet.repository;

import com.mediaplanet.entity.GeneratedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneratedContentRepository
        extends JpaRepository<GeneratedContent, Long>, JpaSpecificationExecutor<GeneratedContent> {
}
