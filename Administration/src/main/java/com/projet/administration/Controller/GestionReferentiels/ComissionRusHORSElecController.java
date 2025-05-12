package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusHORSElec;
import com.projet.administration.Service.GestionReferentiels.ComissionRusHORSElecService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rushorselec")
@RequiredArgsConstructor
public class ComissionRusHORSElecController {

    private final ComissionRusHORSElecService comissionRusHORSElecService;


    @Operation(summary = "Get all ComissionRusHORSElec", description = "Returns a list of all ComissionRusHORSElec entities")
    @GetMapping("/all")
    public ResponseEntity<List<ComissionRusHORSElec>> getAllComissionRusHORSElec() {
        return ResponseEntity.ok(comissionRusHORSElecService.getAllComissionRusHORSElec());
    }

    @Operation(summary = "add ComissionRusHORSElec")
    @PostMapping("/add")
    public ResponseEntity<ComissionRusHORSElec> addComissionRusHORSElec(@RequestBody ComissionRusHORSElec ComissionRusHORSElec) {
        ComissionRusHORSElec createdComissionRusHORSElec = comissionRusHORSElecService.addComissionRusHORSElec(ComissionRusHORSElec);
        return new ResponseEntity<>(createdComissionRusHORSElec, HttpStatus.CREATED);
    }


    @Operation(summary = "update ComissionRusHORSElec")
    @PutMapping("/update/{id}")
    public ResponseEntity<ComissionRusHORSElec> updateComissionRusHORSElec(@PathVariable Long id, @RequestBody ComissionRusHORSElec ComissionRusHORSElec) {
        ComissionRusHORSElec updatedComissionRusHORSElec = comissionRusHORSElecService.updateComissionRusHORSElecWithId(id, ComissionRusHORSElec);
        return updatedComissionRusHORSElec != null ? ResponseEntity.ok(updatedComissionRusHORSElec) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete ComissionRusHORSElec")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteComissionRusHORSElec(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(comissionRusHORSElecService.deleteComissionRusHORSElec(id), HttpStatus.OK);

    }

}
