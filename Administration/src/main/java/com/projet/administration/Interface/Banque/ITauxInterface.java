package com.projet.administration.Interface.Banque;

import com.projet.administration.Entity.Banque.TauxCharge;

import java.util.List;

public interface ITauxInterface {


    public TauxCharge addTauxCharge(TauxCharge tauxCharge) ;
    public TauxCharge updateTauxChargeWithId(Long id, TauxCharge tauxCharge);
    public List<TauxCharge> getAllTauxCharge();
    public String deleteTauxCharge(Long id);
}
