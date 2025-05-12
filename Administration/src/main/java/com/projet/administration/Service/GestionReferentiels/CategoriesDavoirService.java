package com.projet.administration.Service.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.CategoriesDavoir;
import com.projet.administration.Interface.GestionReferentiels.CategoriesDavoirInterface;
import com.projet.administration.Repository.GestionReferentiels.CategoriesDavoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesDavoirService implements CategoriesDavoirInterface {

    private final CategoriesDavoirRepository categoriesDavoirRepository;

    @Override
    public CategoriesDavoir addCategoriesDavoir(CategoriesDavoir CategoriesDavoir) {
        return categoriesDavoirRepository.save(CategoriesDavoir);
    }

    @Override
    public CategoriesDavoir updateCategoriesDavoirWithId(Long id, CategoriesDavoir CategoriesDavoir) {
        return categoriesDavoirRepository.save(CategoriesDavoir);
    }

    @Override
    public List<CategoriesDavoir> getAllCategoriesDavoir() {
        return categoriesDavoirRepository.findAll();
    }

    @Override
    public String deleteCategoriesDavoir(Long id) {
        categoriesDavoirRepository.deleteById(id);
        return "Categories D'avoir deleted with id: " + id;
    }
}
