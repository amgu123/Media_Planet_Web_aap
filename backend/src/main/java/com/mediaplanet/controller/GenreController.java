package com.mediaplanet.controller;

import com.mediaplanet.entity.Genre;
import com.mediaplanet.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@CrossOrigin(origins = "*")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Genre>> getActiveGenres() {
        return ResponseEntity.ok(genreService.getActiveGenres());
    }

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(genre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @RequestBody Genre genre) {
        return ResponseEntity.ok(genreService.updateGenre(id, genre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
