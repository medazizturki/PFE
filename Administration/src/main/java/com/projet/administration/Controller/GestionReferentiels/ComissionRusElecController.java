package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.ComissionRusElec;
import com.projet.administration.Service.GestionReferentiels.ComissionRusElecService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ruselec")
@RequiredArgsConstructor
public class ComissionRusElecController {
    private final ComissionRusElecService comissionRusElecService;


    @Operation(summary = "Get all ComissionRusElec", description = "Returns a list of all ComissionRusElec entities")
    @GetMapping("/all")
    public ResponseEntity<List<ComissionRusElec>> getAllComissionRusElec() {
        return ResponseEntity.ok(comissionRusElecService.getAllComissionRusElec());
    }

    @Operation(summary = "add ComissionRusElec")
    @PostMapping("/add")
    public ResponseEntity<ComissionRusElec> addComissionRusElec(@RequestBody ComissionRusElec ComissionRusElec) {
        ComissionRusElec createdComissionRusElec = comissionRusElecService.addComissionRusElec(ComissionRusElec);
        return new ResponseEntity<>(createdComissionRusElec, HttpStatus.CREATED);
    }


    @Operation(summary = "update ComissionRusElec")
    @PutMapping("/update/{id}")
    public ResponseEntity<ComissionRusElec> updateComissionRusElec(@PathVariable Long id, @RequestBody ComissionRusElec ComissionRusElec) {
        ComissionRusElec updatedComissionRusElec = comissionRusElecService.updateComissionRusElecWithId(id, ComissionRusElec);
        return updatedComissionRusElec != null ? ResponseEntity.ok(updatedComissionRusElec) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete ComissionRusElec")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ComissionRusElec(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(comissionRusElecService.deleteComissionRusElec(id), HttpStatus.OK);

    }

}
