package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.IndiceSectoriel;

import java.util.List;

public interface IndiceSectorielInterface {
    public IndiceSectoriel addIndiceSectoriel(IndiceSectoriel indicesectoriel) ;
    public IndiceSectoriel updateIndiceSectorielWithId(Long id, IndiceSectoriel indicesectoriel);
    public List<IndiceSectoriel> getAllIndiceSectoriel();
    public String deleteIndiceSectoriel(Long id);
}
