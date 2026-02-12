package com.mediaplanet.repository;

import com.mediaplanet.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findByStatus(Boolean status);

    List<Language> findByLanguageNameContainingIgnoreCase(String name);
}
