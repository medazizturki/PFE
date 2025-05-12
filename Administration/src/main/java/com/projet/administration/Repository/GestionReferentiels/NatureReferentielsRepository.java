package com.projet.administration.Repository.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NatureReferentielsRepository extends JpaRepository<NatureReferentiels, Long> {
}
