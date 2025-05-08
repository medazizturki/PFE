package com.projet.administration.Repository.Banque;

import com.projet.administration.Entity.Banque.TMM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TMMRepository extends JpaRepository<TMM, Long> {
}
