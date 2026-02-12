package com.mediaplanet.controller;

import com.mediaplanet.entity.Language;
import com.mediaplanet.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@CrossOrigin(origins = "*")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<Language>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.getLanguageById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Language>> getActiveLanguages() {
        return ResponseEntity.ok(languageService.getActiveLanguages());
    }

    @PostMapping
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.createLanguage(language));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Long id, @RequestBody Language language) {
        return ResponseEntity.ok(languageService.updateLanguage(id, language));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
