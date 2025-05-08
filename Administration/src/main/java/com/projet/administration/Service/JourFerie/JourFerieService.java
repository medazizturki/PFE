package com.projet.administration.Service.JourFerie;

import com.projet.administration.Entity.JourFerie.JourFerie;
import com.projet.administration.Interface.JourFerie.JourFerieInterface;
import com.projet.administration.Repository.JourFerie.JourFerieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JourFerieService implements JourFerieInterface {

    private final JourFerieRepository jourFerieRepository;

    @Override
    public JourFerie addJourFerie(JourFerie jourFerie) {
        return jourFerieRepository.save(jourFerie);
    }

    @Override
    public JourFerie updateJourFerieWithId(Long id, JourFerie jourFerie) {
        return jourFerieRepository.save(jourFerie);
    }

    @Override
    public List<JourFerie> getAllJourFerie() {
        return jourFerieRepository.findAll();
    }

    @Override
    public String deleteJourFerie(Long id) {
        jourFerieRepository.deleteById(id);
        return "Jour Ferie deleted with id: " + id;
    }
}
