package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.CategoriesDavoir;
import com.projet.administration.Service.GestionReferentiels.CategoriesDavoirService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categoriedavoir")
@RequiredArgsConstructor
public class CategoriesDavoirController {

    private final CategoriesDavoirService categoriesDavoirService;


    @Operation(summary = "Get all CategoriesDavoir", description = "Returns a list of all CategoriesDavoir entities")
    @GetMapping("/all")
    public ResponseEntity<List<CategoriesDavoir>> getAllCategoriesDavoir() {
        return ResponseEntity.ok(categoriesDavoirService.getAllCategoriesDavoir());
    }

    @Operation(summary = "add CategoriesDavoir")
    @PostMapping("/add")
    public ResponseEntity<CategoriesDavoir> addCategoriesDavoir(@RequestBody CategoriesDavoir CategoriesDavoir) {
        CategoriesDavoir createdCategoriesDavoir = categoriesDavoirService.addCategoriesDavoir(CategoriesDavoir);
        return new ResponseEntity<>(createdCategoriesDavoir, HttpStatus.CREATED);
    }


    @Operation(summary = "update CategoriesDavoir")
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoriesDavoir> updateCategoriesDavoir(@PathVariable Long id, @RequestBody CategoriesDavoir CategoriesDavoir) {
        CategoriesDavoir updatedCategoriesDavoir = categoriesDavoirService.updateCategoriesDavoirWithId(id, CategoriesDavoir);
        return updatedCategoriesDavoir != null ? ResponseEntity.ok(updatedCategoriesDavoir) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete CategoriesDavoir")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCategoriesDavoir(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(categoriesDavoirService.deleteCategoriesDavoir(id), HttpStatus.OK);

    }


}
