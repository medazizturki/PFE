package com.projet.administration.Service.Banque;

import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Entity.Teneur.CompteTeneur;
import com.projet.administration.Interface.Banque.IDevisesInterface;
import com.projet.administration.Repository.Banque.DevisesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviseService implements IDevisesInterface {

    private final DevisesRepository devisesRepository;

    @Override
    public Devises addDevises(Devises devises) {
        return devisesRepository.save(devises);
    }

    @Override
    public Devises updateDevisesWithId(Long id, Devises devises) {
        return devisesRepository.save(devises);
    }

    @Override
    public List<Devises> getAllDevises() {
        return devisesRepository.findAll();
    }

    @Override
    public String deleteDevises(Long id) {
        devisesRepository.deleteById(id);
        return "devises deleted with id: " + id;
    }
}
