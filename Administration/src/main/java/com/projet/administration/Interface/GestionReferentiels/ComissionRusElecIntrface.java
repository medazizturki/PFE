package com.projet.administration.Interface.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusElec;

import java.util.List;

public interface ComissionRusElecIntrface {
    public ComissionRusElec addComissionRusElec(ComissionRusElec ComissionRusElec) ;
    public ComissionRusElec updateComissionRusElecWithId(Long id, ComissionRusElec ComissionRusElec);
    public List<ComissionRusElec> getAllComissionRusElec();
    public String deleteComissionRusElec(Long id);

}
