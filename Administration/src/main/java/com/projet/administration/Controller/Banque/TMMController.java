package com.projet.administration.Controller.Banque;

import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;
import com.projet.administration.Entity.Banque.TMM;
import com.projet.administration.Service.Banque.TMMService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/tmm")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class TMMController {

    private final TMMService tmmService;

    @Operation(summary = "Get all TMM", description = "Returns a list of all TMM entities")
    @GetMapping("/all")
    public ResponseEntity<List<TMM>> getAllTMM() {
        return ResponseEntity.ok(tmmService.getAllTMM());
    }

    @Operation(summary = "add TMM")
    @PostMapping("/add")
    public ResponseEntity<TMM> addTMM(@RequestBody TMM tmm) {
        TMM createdTMM = tmmService.addTMM(tmm);
        return new ResponseEntity<>(createdTMM, HttpStatus.CREATED);
    }


    @Operation(summary = "update TMM")
    @PutMapping("/update/{id}")
    public ResponseEntity<TMM> updateTMM(@PathVariable Long id, @RequestBody TMM tmm) {
        TMM updatedTMM = tmmService.updateTMMWithId(id, tmm);
        return updatedTMM != null ? ResponseEntity.ok(updatedTMM) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete TMM")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTMM(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(tmmService.deleteTMM(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des TMM au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<TMM> list = tmmService.getAllTMM();

        // 2) Générer le PDF en mémoire
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
        Paragraph titlePara = new Paragraph("Liste des TMM",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau (Mois | TMM)
        PdfPTable table = new PdfPTable(new float[]{4, 3});
        table.setWidthPercentage(100);

        // En-têtes
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell hMois = new PdfPCell(new Phrase("Mois", headFont));
        hMois.setBackgroundColor(new Color(41, 128, 185));
        hMois.setPadding(6);
        PdfPCell hTmm = new PdfPCell(new Phrase("TMM", headFont));
        hTmm.setBackgroundColor(new Color(41, 128, 185));
        hTmm.setPadding(6);
        table.addCell(hMois);
        table.addCell(hTmm);

        // Lignes
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("MM/yyyy");
        for (TMM t : list) {
            // Formater la date du mois (java.util.Date → LocalDate)
            String mois = t.getMois() != null
                    ? monthFmt.format(
                    t.getMois()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
            )
                    : "";
            table.addCell(new PdfPCell(new Phrase(mois, cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(t.getTMM()), cellFont)));
        }
        document.add(table);

        document.close();

        // 3) Préparer un nom de fichier safe Windows
        DateTimeFormatter fileNameFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        String rawTs    = LocalDateTime.now().format(fileNameFmt);
        String safeTs   = rawTs.replace("/", "-").replace(":", ".");
        String fileName = "tmm-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\TMM";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }


}
