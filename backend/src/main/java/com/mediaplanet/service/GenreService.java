package com.mediaplanet.service;

import com.mediaplanet.entity.Genre;
import com.mediaplanet.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));
    }

    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);
        genre.setGenreName(genreDetails.getGenreName());
        genre.setLogo(genreDetails.getLogo());
        genre.setStatus(genreDetails.getStatus());
        return genreRepository.save(genre);
    }

    public void deleteGenre(Long id) {
        Genre genre = getGenreById(id);
        genreRepository.delete(genre);
    }

    public List<Genre> getActiveGenres() {
        return genreRepository.findByStatus(true);
    }
}
