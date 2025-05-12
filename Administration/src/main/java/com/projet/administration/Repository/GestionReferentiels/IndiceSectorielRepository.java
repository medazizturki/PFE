package com.projet.administration.Repository.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.IndiceSectoriel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndiceSectorielRepository extends CrudRepository<IndiceSectoriel, Long> {
}
