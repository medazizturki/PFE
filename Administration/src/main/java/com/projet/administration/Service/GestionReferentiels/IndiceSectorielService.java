package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.IndiceSectoriel;
import com.projet.administration.Interface.GestionReferentiels.IndiceSectorielInterface;
import com.projet.administration.Repository.GestionReferentiels.IndiceSectorielRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndiceSectorielService implements IndiceSectorielInterface {

    private final IndiceSectorielRepository indicesectorielRepository;
    @Override
    public IndiceSectoriel addIndiceSectoriel(IndiceSectoriel indicesectoriel) {
        return indicesectorielRepository.save(indicesectoriel);
    }

    @Override
    public IndiceSectoriel updateIndiceSectorielWithId(Long id, IndiceSectoriel indicesectoriel) {
        return indicesectorielRepository.save(indicesectoriel);
    }

    @Override
    public List<IndiceSectoriel> getAllIndiceSectoriel() {
        return (List<IndiceSectoriel>) indicesectorielRepository.findAll();
    }

    @Override
    public String deleteIndiceSectoriel(Long id) {
        indicesectorielRepository.deleteById(id);
        return "Indice Sectoriel deleted with id: " + id;
    }
}
