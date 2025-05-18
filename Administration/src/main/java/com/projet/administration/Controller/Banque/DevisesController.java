package com.projet.administration.Controller.Banque;

import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.Banque.Devises;
import com.projet.administration.Repository.Banque.DevisesRepository;
import com.projet.administration.Service.Banque.DeviseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.util.List;


import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.IOUtils;
import com.lowagie.text.pdf.PdfWriter;


@RestController
@RequestMapping("/devises")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class DevisesController {

    protected final DeviseService deviseService;

    @Operation(summary = "Get all Devises", description = "Returns a list of all Devises entities")
    @GetMapping("/all")
    public ResponseEntity<List<Devises>> getAllDevises() {
        return ResponseEntity.ok(deviseService.getAllDevises());
    }

    @Operation(summary = "add Devises")
    @PostMapping("/add")
    public ResponseEntity<Devises> addDevises(@RequestBody Devises devises) {
        Devises createdDevises = deviseService.addDevises(devises);
        return new ResponseEntity<>(createdDevises, HttpStatus.CREATED);
    }


    @Operation(summary = "update Devises")
    @PutMapping("/update/{id}")
    public ResponseEntity<Devises> updateDevises(@PathVariable Long id, @RequestBody Devises devises) {
        Devises updatedDevises = deviseService.updateDevisesWithId(id, devises);
        return updatedDevises != null ? ResponseEntity.ok(updatedDevises) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete Devises")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDevises(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(deviseService.deleteDevises(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des Devises au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<Devises> list = deviseService.getAllDevises();

        // 2) Générer le PDF en mémoire
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo
        String logoPath = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\src\\assets\\uploads-images\\aaaa.jpeg";
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(50, 50);
        float pageHeight = document.getPageSize().getHeight();
        // Remonter le logo : 50pt sous le bord supérieur
        logo.setAbsolutePosition(36, pageHeight - 70);
        document.add(logo);

        // 2.b) Date à droite
        String dateStr = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Phrase datePhrase = new Phrase("Généré le : " + dateStr,
                FontFactory.getFont(FontFactory.HELVETICA, 10)
        );
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_RIGHT,
                datePhrase,
                document.getPageSize().getWidth() - 36,
                pageHeight - 40,
                0
        );

        // 2.c) Titre centré
        Paragraph titlePara = new Paragraph("Liste des Devises",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau (Code | Libellé FR | Libellé Cfr | Libellé AR | Libellé Car | Libellé EN | Libellé Cen)
        PdfPTable table = new PdfPTable(new float[]{2, 3, 3, 3, 3, 3, 3});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code", "Libellé FR", "Libellé Cfr", "Libellé AR",
                "Libellé Car", "Libellé EN", "Libellé Cen"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (Devises d : list) {
            table.addCell(new PdfPCell(new Phrase(d.getCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibelleCfr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibellear(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibelleCar(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibelleen(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(d.getLibelleCen(), cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Préparer un nom de fichier safe Windows
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        String rawTs  = LocalDateTime.now().format(fmt);
        String safeTs = rawTs.replace("/", "-").replace(":", ".");
        String fileName = "devises-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Devises";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client pour téléchargement
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }

}
