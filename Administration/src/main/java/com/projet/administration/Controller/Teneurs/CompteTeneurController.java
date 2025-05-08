package com.projet.administration.Controller.Teneurs;

import com.projet.administration.Entity.Teneur.CompteTeneur;
import com.projet.administration.Service.Teneurs.CompteTeneurService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/compteteneur")
@RequiredArgsConstructor
public class CompteTeneurController {

    private final CompteTeneurService compteTeneurService;

    @Operation(summary = "Get all CompteTeneur", description = "Returns a list of all CompteTeneur entities")
    @GetMapping("/all")
    public ResponseEntity<List<CompteTeneur>> getAllCompteTeneur() {
        return ResponseEntity.ok(compteTeneurService.getAllCompteTeneur());
    }

    @Operation(summary = "add CompteTeneur")
    @PostMapping("/add")
    public ResponseEntity<CompteTeneur> addTCompteTeneur(@RequestBody CompteTeneur compteTeneur) {
        CompteTeneur created = compteTeneurService.addCompteTeneur(compteTeneur);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @Operation(summary = "update CompteTeneur")
    @PutMapping("/update/{id}")
    public ResponseEntity<CompteTeneur> updateCompteTeneur(@PathVariable Long id, @RequestBody CompteTeneur compteTeneur) {
        CompteTeneur updatedCompteTeneur = compteTeneurService.updateCompteTeneurWithId(id, compteTeneur);
        return updatedCompteTeneur != null ? ResponseEntity.ok(updatedCompteTeneur) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete CompteTeneur")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCompteTeneur(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(compteTeneurService.deleteCompteTeneur(id), HttpStatus.OK);

    }
}
