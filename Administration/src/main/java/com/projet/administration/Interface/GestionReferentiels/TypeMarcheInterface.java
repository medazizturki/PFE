package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.TypeMarche;

import java.util.List;

public interface TypeMarcheInterface {

    public TypeMarche addTypeMarche(TypeMarche typemarche) ;
    public TypeMarche updateTypeMarcheWithId(Long id, TypeMarche typemarche);
    public List<TypeMarche> getAllTypeMarche();
    public String deleteTypeMarche(Long id);

}
