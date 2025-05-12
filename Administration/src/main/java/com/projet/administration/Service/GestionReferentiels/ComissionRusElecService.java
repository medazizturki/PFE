package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusElec;
import com.projet.administration.Interface.GestionReferentiels.ComissionRusElecIntrface;
import com.projet.administration.Repository.GestionReferentiels.ComissionRusElecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComissionRusElecService implements ComissionRusElecIntrface {

    private final ComissionRusElecRepository comissionRusElecRepository;

    @Override
    public ComissionRusElec addComissionRusElec(ComissionRusElec ComissionRusElec) {
        return comissionRusElecRepository.save(ComissionRusElec);
    }

    @Override
    public ComissionRusElec updateComissionRusElecWithId(Long id, ComissionRusElec ComissionRusElec) {
        return comissionRusElecRepository.save(ComissionRusElec);
    }

    @Override
    public List<ComissionRusElec> getAllComissionRusElec() {
        return comissionRusElecRepository.findAll();
    }

    @Override
    public String deleteComissionRusElec(Long id) {
        comissionRusElecRepository.deleteById(id);
        return "Comission Rus Elec deleted with id: " + id;
    }
}
