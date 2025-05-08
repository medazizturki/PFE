package com.projet.administration.Service.Banque;

import com.projet.administration.Entity.Banque.TMM;
import com.projet.administration.Interface.Banque.ITMMInterface;
import com.projet.administration.Repository.Banque.TMMRepository;
import com.projet.administration.Repository.Teneurs.CompteTeneurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TMMService implements ITMMInterface {


    private final TMMRepository tmmRepository;


    @Override
    public TMM addTMM(TMM tmm) {
        return tmmRepository.save(tmm);
    }

    @Override
    public TMM updateTMMWithId(Long id, TMM tmm) {
        return tmmRepository.save(tmm);
    }

    @Override
    public List<TMM> getAllTMM() {
        return tmmRepository.findAll();
    }

    @Override
    public String deleteTMM(Long id) {
        tmmRepository.deleteById(id);
        return "tmm deleted with id: " + id;
    }




}
