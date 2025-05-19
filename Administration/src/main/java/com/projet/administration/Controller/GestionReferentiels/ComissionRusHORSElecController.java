package com.projet.administration.Controller.GestionReferentiels;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.GestionReferentiels.ComissionRusHORSElec;
import com.projet.administration.Service.GestionReferentiels.ComissionRusHORSElecService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.awt.*;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/rushorselec")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
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


    @Operation(summary = "Exporter la liste des ComissionRusHORSElec au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<ComissionRusHORSElec> list = comissionRusHORSElecService.getAllComissionRusHORSElec();

        // 2) Générer le PDF en mémoire, A4 portrait
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
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

        // 2.b) Date alignée à droite
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

        Paragraph titlePara = new Paragraph("Liste des Commissions Rus HORS Electronique",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingBefore(20);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau (Titre | Type | Rate | Minimum | Maximum)
        PdfPTable table = new PdfPTable(new float[]{5, 4, 3, 3, 3});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = { "Titre", "Type", "Taux", "Minimum", "Maximum" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (ComissionRusHORSElec c : list) {
            table.addCell(new PdfPCell(new Phrase(c.getTitre(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(c.getType(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getRate()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getMinimum()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getMaximum()), cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Préparer un nom de fichier safe Windows
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "rushorselec-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\ComissionRusHORSElec";
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
