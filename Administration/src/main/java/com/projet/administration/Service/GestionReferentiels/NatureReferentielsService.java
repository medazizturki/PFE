package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;
import com.projet.administration.Interface.GestionReferentiels.NatureReferentielsInterface;
import com.projet.administration.Repository.GestionReferentiels.NatureReferentielsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NatureReferentielsService implements NatureReferentielsInterface {

    private final NatureReferentielsRepository natureReferentielsRepository;

    @Override
    public NatureReferentiels addNatureReferentiels(NatureReferentiels natureReferentiels) {
        return natureReferentielsRepository.save(natureReferentiels);
    }

    @Override
    public NatureReferentiels updateNatureReferentielsWithId(Long id, NatureReferentiels natureReferentiels) {
        return natureReferentielsRepository.save(natureReferentiels);
    }

    @Override
    public List<NatureReferentiels> getAllNatureReferentiels() {
        return natureReferentielsRepository.findAll();
    }

    @Override
    public String deleteNatureReferentiels(Long id) {
        natureReferentielsRepository.deleteById(id);
        return "Nature Referentiels deleted with id: " + id;
    }
}
