package com.projet.administration.Repository.Teneurs;

import com.projet.administration.Entity.Teneur.TypeTeneur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeTeneurRepository extends JpaRepository<TypeTeneur, Long> {
}
