package com.projet.administration.Interface.Teneurs;

import com.projet.administration.Entity.Teneur.CompteTeneur;

import java.util.List;

public interface ICompteTeneur {

    public CompteTeneur addCompteTeneur(CompteTeneur compteTeneur) ;
    public CompteTeneur updateCompteTeneurWithId(Long id, CompteTeneur compteTeneur);
    public List<CompteTeneur> getAllCompteTeneur();
    public String deleteCompteTeneur(Long id);
}
