package com.mediaplanet.service;

import com.mediaplanet.entity.Language;
import com.mediaplanet.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language getLanguageById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Language not found with id: " + id));
    }

    public Language createLanguage(Language language) {
        return languageRepository.save(language);
    }

    public Language updateLanguage(Long id, Language languageDetails) {
        Language language = getLanguageById(id);
        language.setLanguageName(languageDetails.getLanguageName());
        language.setLogo(languageDetails.getLogo());
        language.setStatus(languageDetails.getStatus());
        return languageRepository.save(language);
    }

    public void deleteLanguage(Long id) {
        Language language = getLanguageById(id);
        languageRepository.delete(language);
    }

    public List<Language> getActiveLanguages() {
        return languageRepository.findByStatus(true);
    }
}
