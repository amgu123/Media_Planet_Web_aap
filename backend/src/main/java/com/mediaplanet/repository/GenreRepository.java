package com.mediaplanet.repository;

import com.mediaplanet.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByStatus(Boolean status);

    List<Genre> findByGenreNameContainingIgnoreCase(String name);
}
