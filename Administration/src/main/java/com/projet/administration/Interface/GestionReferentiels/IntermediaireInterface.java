package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Intermediaire;

import java.util.List;

public interface IntermediaireInterface {


    public Intermediaire addIntermediaire(Intermediaire Intermediaire) ;
    public Intermediaire updateIntermediaireWithId(Long id, Intermediaire Intermediaire);
    public List<Intermediaire> getAllIntermediaire();
    public String deleteIntermediaire(Long id);
}
