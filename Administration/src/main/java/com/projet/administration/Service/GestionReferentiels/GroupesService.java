package com.projet.administration.Service.GestionReferentiels;


import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Entity.Teneur.TypeTeneur;
import com.projet.administration.Interface.GestionReferentiels.GroupeInterface;
import com.projet.administration.Repository.Banque.DevisesRepository;
import com.projet.administration.Repository.GestionReferentiels.GroupeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupesService implements GroupeInterface {

    private final GroupeRepository groupeRepository;
    private final DevisesRepository devisesRepository;

    @Override
    public Groupes addGroupes(Groupes groupes) {
        if (groupes.getDevises() != null && groupes.getDevises().getId() != null) {
            Devises fullDevises = devisesRepository.findById(groupes.getDevises().getId())
                    .orElseThrow(() -> new RuntimeException("Devises introuvable"));
            groupes.setDevises(fullDevises);
        } else {
            groupes.setDevises(null); // 🔒 sécurité si jamais null envoyé
        }

        return groupeRepository.save(groupes);
    }

    @Override
    public Groupes updateGroupesWithId(Long id, Groupes groupes) {
        return groupeRepository.save(groupes);
    }

    @Override
    public List<Groupes> getAllGroupes() {
        return groupeRepository.findAll();
    }

    @Override
    public String deleteGroupes(Long id) {
        groupeRepository.deleteById(id);
        return "groupe deleted with id: " + id;
    }
}
