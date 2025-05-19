package com.projet.administration.Interface.Secteur;

import com.projet.administration.Entity.Secteur.SecteurInternational;

import java.util.List;

public interface SecteurInternationalInterface {


    public SecteurInternational addSecteurInternational(SecteurInternational SecteurInternational) ;
    public SecteurInternational updateSecteurInternationalWithId(Long id, SecteurInternational SecteurInternational);
    public List<SecteurInternational> getAllSecteurInternational();
    public String deleteSecteurInternational(Long id);
}
