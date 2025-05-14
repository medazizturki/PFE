package com.projet.administration.Service.Banque;


import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Entity.Banque.TauxCharge;
import com.projet.administration.Interface.Banque.ITMMInterface;
import com.projet.administration.Interface.Banque.ITauxInterface;
import com.projet.administration.Repository.Banque.DevisesRepository;
import com.projet.administration.Repository.Banque.TauxChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TauxChargeService implements ITauxInterface {

    private final TauxChargeRepository tauxChargeRepository;
    private final DevisesRepository devisesRepository;

    @Override
    public TauxCharge addTauxCharge(TauxCharge tauxCharge) {
        if (tauxCharge.getDevises() != null && tauxCharge.getDevises().getId() != null) {
            Devises existingDevise = devisesRepository.findById(tauxCharge.getDevises().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Devise introuvable avec l'ID " + tauxCharge.getDevises().getId()));
            tauxCharge.setDevises(existingDevise);
        }
        return tauxChargeRepository.save(tauxCharge);
    }

    @Override
    public TauxCharge updateTauxChargeWithId(Long id, TauxCharge tauxCharge) {
        if (tauxCharge.getDevises() != null && tauxCharge.getDevises().getId() != null) {
            Devises existingDevise = devisesRepository.findById(tauxCharge.getDevises().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Devise introuvable avec l'ID " + tauxCharge.getDevises().getId()));
            tauxCharge.setDevises(existingDevise);
        }
        tauxCharge.setId(id); // important pour mise à jour
        return tauxChargeRepository.save(tauxCharge);
    }

    @Override
    public List<TauxCharge> getAllTauxCharge() {
        List<TauxCharge> list = tauxChargeRepository.findAll();
        System.out.println("🎯 Nombre de TauxCharge trouvés : " + list.size());
        list.forEach(tc -> {
            System.out.println("✅ TauxCharge ID: " + tc.getId() + ", Devise: " + (tc.getDevises() != null ? tc.getDevises().getCode() : "NULL"));
        });
        return list;
    }


    @Override
    public String deleteTauxCharge(Long id) {
        tauxChargeRepository.deleteById(id);
        return "Taux de charge deleted with id: " + id;
    }
}
