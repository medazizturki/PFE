package com.projet.administration.Service.Teneurs;

import com.projet.administration.Entity.Teneur.CompteTeneur;
import com.projet.administration.Entity.Teneur.TypeTeneur;
import com.projet.administration.Interface.Teneurs.ICompteTeneur;
import com.projet.administration.Repository.Teneurs.CompteTeneurRepository;
import com.projet.administration.Repository.Teneurs.TypeTeneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompteTeneurService implements ICompteTeneur {

    private final CompteTeneurRepository compteTeneurRepository;
    private final TypeTeneurRepository typeTeneurRepository;

    @Override
    public CompteTeneur addCompteTeneur(CompteTeneur compteTeneur) {
        // 🔥 Corriger la relation ManyToOne manuellement
        if (compteTeneur.getTypeTeneur() != null && compteTeneur.getTypeTeneur().getId() != null) {
            TypeTeneur fullType = typeTeneurRepository.findById(compteTeneur.getTypeTeneur().getId())
                    .orElseThrow(() -> new RuntimeException("TypeTeneur introuvable"));
            compteTeneur.setTypeTeneur(fullType);
        } else {
            compteTeneur.setTypeTeneur(null); // 🔒 sécurité si jamais null envoyé
        }

        return compteTeneurRepository.save(compteTeneur);
    }

    @Override
    public CompteTeneur updateCompteTeneurWithId(Long id, CompteTeneur compteTeneur) {
        // 🔁 Attacher de nouveau l’objet TypeTeneur avant update
        if (compteTeneur.getTypeTeneur() != null && compteTeneur.getTypeTeneur().getId() != null) {
            TypeTeneur fullType = typeTeneurRepository.findById(compteTeneur.getTypeTeneur().getId())
                    .orElseThrow(() -> new RuntimeException("TypeTeneur introuvable"));
            compteTeneur.setTypeTeneur(fullType);
        } else {
            compteTeneur.setTypeTeneur(null);
        }

        compteTeneur.setId(id); // pour s'assurer qu'on update et pas insert
        return compteTeneurRepository.save(compteTeneur);
    }

    @Override
    public List<CompteTeneur> getAllCompteTeneur() {
        return compteTeneurRepository.findAll();
    }

    @Override
    public String deleteCompteTeneur(Long id) {
        compteTeneurRepository.deleteById(id);
        return "Compte Teneur deleted with id: " + id;
    }
}
