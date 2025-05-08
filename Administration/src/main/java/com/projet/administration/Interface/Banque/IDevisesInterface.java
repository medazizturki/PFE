package com.projet.administration.Interface.Banque;

import com.projet.administration.Entity.Banque.Devises;
import java.util.List;

public interface IDevisesInterface {


    public Devises addDevises(Devises devises) ;
    public Devises updateDevisesWithId(Long id, Devises devises);
    public List<Devises> getAllDevises();
    public String deleteDevises(Long id);
}
