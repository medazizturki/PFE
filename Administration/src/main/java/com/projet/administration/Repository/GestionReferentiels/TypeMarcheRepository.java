package com.projet.administration.Repository.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.TypeMarche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMarcheRepository extends JpaRepository<TypeMarche, Long> {
}
