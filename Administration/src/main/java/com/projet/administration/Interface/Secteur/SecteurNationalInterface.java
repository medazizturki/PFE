package com.projet.administration.Interface.Secteur;

import com.projet.administration.Entity.Secteur.SecteurNational;

import java.util.List;

public interface SecteurNationalInterface {

    public SecteurNational addSecteurNational(SecteurNational SecteurNational) ;
    public SecteurNational updateSecteurNationalWithId(Long id, SecteurNational SecteurNational);
    public List<SecteurNational> getAllSecteurNational();
    public String deleteSecteurNational(Long id);

}
