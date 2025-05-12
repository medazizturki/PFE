package com.projet.administration.Repository.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusHORSElec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissionRusHORSElecRepository extends JpaRepository<ComissionRusHORSElec, Long> {
}
