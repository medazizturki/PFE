package com.projet.administration.Repository.JourFerie;

import com.projet.administration.Entity.JourFerie.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {
}
