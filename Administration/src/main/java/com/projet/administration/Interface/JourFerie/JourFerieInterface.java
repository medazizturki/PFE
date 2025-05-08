package com.projet.administration.Interface.JourFerie;

import com.projet.administration.Entity.JourFerie.JourFerie;

import java.util.List;

public interface JourFerieInterface {

    public JourFerie addJourFerie(JourFerie jourFerie) ;
    public JourFerie updateJourFerieWithId(Long id, JourFerie jourFerie);
    public List<JourFerie> getAllJourFerie();
    public String deleteJourFerie(Long id);
}
