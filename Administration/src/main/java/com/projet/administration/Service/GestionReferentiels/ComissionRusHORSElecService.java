package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusHORSElec;
import com.projet.administration.Interface.GestionReferentiels.ComissionRusHORSElecInterface;
import com.projet.administration.Repository.GestionReferentiels.ComissionRusHORSElecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComissionRusHORSElecService implements ComissionRusHORSElecInterface {

    private final ComissionRusHORSElecRepository comissionRusHORSElecRepository;

    @Override
    public ComissionRusHORSElec addComissionRusHORSElec(ComissionRusHORSElec ComissionRusHORSElec) {
        return comissionRusHORSElecRepository.save(ComissionRusHORSElec);
    }

    @Override
    public ComissionRusHORSElec updateComissionRusHORSElecWithId(Long id, ComissionRusHORSElec ComissionRusHORSElec) {
        return comissionRusHORSElecRepository.save(ComissionRusHORSElec);
    }

    @Override
    public List<ComissionRusHORSElec> getAllComissionRusHORSElec() {
        return comissionRusHORSElecRepository.findAll();
    }

    @Override
    public String deleteComissionRusHORSElec(Long id) {
        comissionRusHORSElecRepository.deleteById(id);
        return "comissionRusHORSElec with id " + id + " was deleted";
    }
}
