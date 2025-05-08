package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.Groupes;

import java.util.List;

public interface GroupeInterface {

    public Groupes addGroupes(Groupes groupes) ;
    public Groupes updateGroupesWithId(Long id, Groupes groupes);
    public List<Groupes> getAllGroupes();
    public String deleteGroupes(Long id);
}
