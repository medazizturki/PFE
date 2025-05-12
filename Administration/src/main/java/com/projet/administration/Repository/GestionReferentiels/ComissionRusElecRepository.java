package com.projet.administration.Repository.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusElec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissionRusElecRepository extends JpaRepository<ComissionRusElec, Long> {
}
