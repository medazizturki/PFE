package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;

import java.util.List;

public interface NatureReferentielsInterface {

    public NatureReferentiels addNatureReferentiels(NatureReferentiels natureReferentiels) ;
    public NatureReferentiels updateNatureReferentielsWithId(Long id, NatureReferentiels natureReferentiels);
    public List<NatureReferentiels> getAllNatureReferentiels();
    public String deleteNatureReferentiels(Long id);
}
