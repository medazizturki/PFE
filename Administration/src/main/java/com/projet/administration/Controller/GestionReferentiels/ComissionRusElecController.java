package com.projet.administration.Controller.GestionReferentiels;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.GestionReferentiels.ComissionRusElec;
import com.projet.administration.Service.GestionReferentiels.ComissionRusElecService;
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
@RequestMapping("ruselec")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
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

    @Operation(summary = "Exporter la liste des ComissionRusElec au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<ComissionRusElec> list = comissionRusElecService.getAllComissionRusElec();

        // 2) Générer le PDF en mémoire, A4 portrait
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

        // 2.b) Date alignée à droite, position fixe à 50pt sous le bord supérieur
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
        Paragraph titlePara = new Paragraph("Liste des Commissions RUS/CTB",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau
        PdfPTable table = new PdfPTable(new float[]{3,2,2,2,2,2,2,2});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Groupe", "Rang",
                "Taux CTB", "Min CTB", "Max CTB",
                "Taux RUS","Min RUS","Max RUS"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41,128,185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (ComissionRusElec c : list) {
            table.addCell(new PdfPCell(new Phrase(c.getGroupe(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getRang()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxCTB()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getValeurminCTB()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getValeurmaxCTB()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getTauxRUS()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getValeurminRUS()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(c.getValeurmaxRUS()), cellFont)));
        }
        document.add(table);
        document.close();

        // 3) Nom de fichier Windows-safe
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "rushorselec-" + ts + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\RusElec";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }

}
