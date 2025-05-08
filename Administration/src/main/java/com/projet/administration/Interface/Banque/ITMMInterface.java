package com.projet.administration.Interface.Banque;

import com.projet.administration.Entity.Banque.TMM;

import java.util.List;

public interface ITMMInterface {

    public TMM addTMM(TMM tmm) ;
    public TMM updateTMMWithId(Long id, TMM tmm);
    public List<TMM> getAllTMM();
    public String deleteTMM(Long id);
}
