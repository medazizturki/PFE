package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Intermediaire;
import com.projet.administration.Interface.GestionReferentiels.IntermediaireInterface;
import com.projet.administration.Repository.GestionReferentiels.IntermediaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntermediaireService implements IntermediaireInterface {

    private final IntermediaireRepository intermediaireRepository;

    @Override
    public Intermediaire addIntermediaire(Intermediaire Intermediaire) {
        return intermediaireRepository.save(Intermediaire);
    }

    @Override
    public Intermediaire updateIntermediaireWithId(Long id, Intermediaire Intermediaire) {
        return intermediaireRepository.save(Intermediaire);
    }

    @Override
    public List<Intermediaire> getAllIntermediaire() {
        return intermediaireRepository.findAll();
    }

    @Override
    public String deleteIntermediaire(Long id) {
        intermediaireRepository.deleteById(id);
        return "Intermediaire with id " + id + " was deleted";
    }
}
