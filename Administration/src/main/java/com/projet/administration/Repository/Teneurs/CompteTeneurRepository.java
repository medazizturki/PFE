package com.projet.administration.Repository.Teneurs;

import com.projet.administration.Entity.Teneur.CompteTeneur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteTeneurRepository extends JpaRepository<CompteTeneur, Long> {
}
