package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusHORSElec;

import java.util.List;

public interface ComissionRusHORSElecInterface {
    public ComissionRusHORSElec addComissionRusHORSElec(ComissionRusHORSElec ComissionRusHORSElec) ;
    public ComissionRusHORSElec updateComissionRusHORSElecWithId(Long id, ComissionRusHORSElec ComissionRusHORSElec);
    public List<ComissionRusHORSElec> getAllComissionRusHORSElec();
    public String deleteComissionRusHORSElec(Long id);

}
