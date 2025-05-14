package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.NatureReferentiels;
import com.projet.administration.Service.GestionReferentiels.NatureReferentielsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/naturereferentiels")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class NatureReferentielsController {

    private final NatureReferentielsService natureReferentielsService;

    @Operation(summary = "Get all NatureReferentiels", description = "Returns a list of all NatureReferentiels entities")
    @GetMapping("/all")
    public ResponseEntity<List<NatureReferentiels>> getAllNatureReferentiels() {
        return ResponseEntity.ok(natureReferentielsService.getAllNatureReferentiels());
    }

    @Operation(summary = "add NatureReferentiels")
    @PostMapping("/add")
    public ResponseEntity<NatureReferentiels> addNatureReferentiels(@RequestBody NatureReferentiels naturereferentiels) {
        NatureReferentiels createdNatureReferentiels = natureReferentielsService.addNatureReferentiels(naturereferentiels);
        return new ResponseEntity<>(createdNatureReferentiels, HttpStatus.CREATED);
    }


    @Operation(summary = "update NatureReferentiels")
    @PutMapping("/update/{id}")
    public ResponseEntity<NatureReferentiels> updateNatureReferentiels(@PathVariable Long id, @RequestBody NatureReferentiels naturereferentiels) {
        NatureReferentiels updatedNatureReferentiels = natureReferentielsService.updateNatureReferentielsWithId(id, naturereferentiels);
        return updatedNatureReferentiels != null ? ResponseEntity.ok(updatedNatureReferentiels) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete NatureReferentiels")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteNatureReferentiels(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(natureReferentielsService.deleteNatureReferentiels(id), HttpStatus.OK);

    }


}
