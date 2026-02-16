package com.mediaplanet.repository;

import com.mediaplanet.entity.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscriptRepository
        extends JpaRepository<Transcript, Long>, JpaSpecificationExecutor<Transcript> {

    List<Transcript> findByGeneratedContentId(Long generatedContentId);
}
