package com.projet.administration.Service.Banque;


import com.projet.administration.Entity.Banque.TauxCharge;
import com.projet.administration.Interface.Banque.ITMMInterface;
import com.projet.administration.Interface.Banque.ITauxInterface;
import com.projet.administration.Repository.Banque.TauxChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TauxChargeService implements ITauxInterface {

    private final TauxChargeRepository tauxChargeRepository;

    @Override
    public TauxCharge addTauxCharge(TauxCharge tauxCharge) {
        return tauxChargeRepository.save(tauxCharge);
    }

    @Override
    public TauxCharge updateTauxChargeWithId(Long id, TauxCharge tauxCharge) {
        return tauxChargeRepository.save(tauxCharge);
    }

    @Override
    public List<TauxCharge> getAllTauxCharge() {
        return tauxChargeRepository.findAll();
    }

    @Override
    public String deleteTauxCharge(Long id) {
        tauxChargeRepository.deleteById(id);
        return "Taux de charge deleted with id: " + id;
    }
}
