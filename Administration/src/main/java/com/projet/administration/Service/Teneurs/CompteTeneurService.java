package com.projet.administration.Service.Teneurs;

import com.projet.administration.Entity.Teneur.CompteTeneur;
import com.projet.administration.Interface.Teneurs.ICompteTeneur;
import com.projet.administration.Repository.Teneurs.CompteTeneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompteTeneurService implements ICompteTeneur {

    private final CompteTeneurRepository compteTeneurRepository;
    @Override
    public CompteTeneur addCompteTeneur(CompteTeneur compteTeneur) {
        return compteTeneurRepository.save(compteTeneur);
    }

    @Override
    public CompteTeneur updateCompteTeneurWithId(Long id, CompteTeneur compteTeneur) {
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
