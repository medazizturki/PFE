package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.CategoriesDavoir;

import java.util.List;

public interface CategoriesDavoirInterface {
    public CategoriesDavoir addCategoriesDavoir(CategoriesDavoir CategoriesDavoir) ;
    public CategoriesDavoir updateCategoriesDavoirWithId(Long id, CategoriesDavoir CategoriesDavoir);
    public List<CategoriesDavoir> getAllCategoriesDavoir();
    public String deleteCategoriesDavoir(Long id);

}
