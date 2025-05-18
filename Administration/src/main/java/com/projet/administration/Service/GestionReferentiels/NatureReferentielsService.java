package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Entity.GestionReferentiels.Groupes;
import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;
import com.projet.administration.Interface.GestionReferentiels.NatureReferentielsInterface;
import com.projet.administration.Repository.GestionReferentiels.GroupeRepository;
import com.projet.administration.Repository.GestionReferentiels.NatureReferentielsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NatureReferentielsService implements NatureReferentielsInterface {

    private final NatureReferentielsRepository natureReferentielsRepository;
    private final GroupeRepository groupeRepository;

    @Override
    public NatureReferentiels addNatureReferentiels(NatureReferentiels natureReferentiels) {
        // 🔥 Corriger la relation ManyToOne manuellement
        if (natureReferentiels.getGroupes() != null && natureReferentiels.getGroupes().getId() != null) {
            Groupes fullGroupes = groupeRepository.findById(natureReferentiels.getGroupes().getId())
                    .orElseThrow(() -> new RuntimeException("Devises introuvable"));
            natureReferentiels.setGroupes(fullGroupes);
        } else {
            natureReferentiels.setGroupes(null); // 🔒 sécurité si jamais null envoyé
        }

        return natureReferentielsRepository.save(natureReferentiels);
    }

    @Override
    public NatureReferentiels updateNatureReferentielsWithId(Long id, NatureReferentiels natureReferentiels) {
        return natureReferentielsRepository.save(natureReferentiels);
    }

    @Override
    public List<NatureReferentiels> getAllNatureReferentiels() {
        return natureReferentielsRepository.findAll();
    }

    @Override
    public String deleteNatureReferentiels(Long id) {
        natureReferentielsRepository.deleteById(id);
        return "Nature Referentiels deleted with id: " + id;
    }
}
