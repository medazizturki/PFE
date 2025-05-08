package com.projet.administration.Repository.Banque;

import com.projet.administration.Entity.Banque.TauxCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TauxChargeRepository extends JpaRepository<TauxCharge, Long> {
}
