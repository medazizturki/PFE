package com.projet.administration.Repository.Banque;

import com.projet.administration.Entity.Banque.Devises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisesRepository extends JpaRepository<Devises, Long> {
}
