package com.projet.administration.Service.Secteur;

import com.projet.administration.Entity.Secteur.SecteurNational;
import com.projet.administration.Interface.Secteur.SecteurNationalInterface;
import com.projet.administration.Repository.Secteur.SecteurNationalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SecteurNationalService implements SecteurNationalInterface {

        private final SecteurNationalRepository secteurNationalRepository;
    @Override
    public SecteurNational addSecteurNational(SecteurNational SecteurNational) {
        return secteurNationalRepository.save(SecteurNational);
    }

    @Override
    public SecteurNational updateSecteurNationalWithId(Long id, SecteurNational SecteurNational) {
        return secteurNationalRepository.save(SecteurNational);
    }

    @Override
    public List<SecteurNational> getAllSecteurNational() {
        return secteurNationalRepository.findAll();
    }

    @Override
    public String deleteSecteurNational(Long id) {
        secteurNationalRepository.deleteById(id);
        return "SecteurNational with id " + id + " was deleted";
    }
}
