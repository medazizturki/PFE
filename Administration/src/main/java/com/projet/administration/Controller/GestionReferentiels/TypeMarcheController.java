package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.TypeMarche;
import com.projet.administration.Service.GestionReferentiels.TypeMarcheService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/typemarche")
@RequiredArgsConstructor
public class TypeMarcheController {

    private final TypeMarcheService typeMarcheService;


    @Operation(summary = "Get all TypeMarche", description = "Returns a list of all TypeMarche entities")
    @GetMapping("/all")
    public ResponseEntity<List<TypeMarche>> getAllTypeMarche() {
        return ResponseEntity.ok(typeMarcheService.getAllTypeMarche());
    }

    @Operation(summary = "add TypeMarche")
    @PostMapping("/add")
    public ResponseEntity<TypeMarche> addTypeMarche(@RequestBody TypeMarche typemarche) {
        TypeMarche createdTypeMarche = typeMarcheService.addTypeMarche(typemarche);
        return new ResponseEntity<>(createdTypeMarche, HttpStatus.CREATED);
    }


    @Operation(summary = "update TypeMarche")
    @PutMapping("/update/{id}")
    public ResponseEntity<TypeMarche> updateTypeMarche(@PathVariable Long id, @RequestBody TypeMarche typemarche) {
        TypeMarche updatedTypeMarche = typeMarcheService.updateTypeMarcheWithId(id, typemarche);
        return updatedTypeMarche != null ? ResponseEntity.ok(updatedTypeMarche) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete TypeMarche")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTypeMarche(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(typeMarcheService.deleteTypeMarche(id), HttpStatus.OK);

    }

}
