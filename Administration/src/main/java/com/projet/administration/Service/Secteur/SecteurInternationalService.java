package com.projet.administration.Service.Secteur;

import com.projet.administration.Entity.Secteur.SecteurInternational;
import com.projet.administration.Interface.Secteur.SecteurInternationalInterface;
import com.projet.administration.Repository.Secteur.SecteurInternationalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecteurInternationalService implements SecteurInternationalInterface {

        private final SecteurInternationalRepository secteurInternationalRepository;
    @Override
    public SecteurInternational addSecteurInternational(SecteurInternational SecteurInternational) {
        return secteurInternationalRepository.save(SecteurInternational);
    }

    @Override
    public SecteurInternational updateSecteurInternationalWithId(Long id, SecteurInternational SecteurInternational) {
        return secteurInternationalRepository.save(SecteurInternational);
    }

    @Override
    public List<SecteurInternational> getAllSecteurInternational() {
        return secteurInternationalRepository.findAll();
    }

    @Override
    public String deleteSecteurInternational(Long id) {
        secteurInternationalRepository.deleteById(id);
        return "Secteur international with id " + id + " was deleted";
    }
}
