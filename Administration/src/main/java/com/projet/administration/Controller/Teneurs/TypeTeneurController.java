package com.projet.administration.Controller.Teneurs;

import com.projet.administration.Entity.Teneur.TypeTeneur;
import com.projet.administration.Service.Teneurs.TypeTeneurService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/typeteneur")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class TypeTeneurController {


    private final TypeTeneurService typeTeneurService;

    @Operation(summary = "Get all TypeTeneurs", description = "Returns a list of all TypeTeneur")
    @GetMapping("/all")
    public ResponseEntity<List<TypeTeneur>> getAllTypeTeneur() {
        return ResponseEntity.ok(typeTeneurService.getAllTypeTeneur());
    }

    @Operation(summary = "add TypeTeneurs")
    @PostMapping("/add")
    public ResponseEntity<TypeTeneur> addTypeTeneur(@RequestBody TypeTeneur typeTeneur) {
        TypeTeneur created = typeTeneurService.addTypeTeneur(typeTeneur);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @Operation(summary = "update TypeTeneurs")
    @PutMapping("/update/{id}")
    public ResponseEntity<TypeTeneur> updateTypeTeneur(@PathVariable Long id, @RequestBody TypeTeneur typeTeneur) {
        TypeTeneur updatedTypeTeneur = typeTeneurService.updateTypeTeneurWithId(id, typeTeneur);
        return updatedTypeTeneur != null ? ResponseEntity.ok(updatedTypeTeneur) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete TypeTeneur")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTypeTeneur(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(typeTeneurService.deleteTypeTeneur(id), HttpStatus.OK);

    }
}
