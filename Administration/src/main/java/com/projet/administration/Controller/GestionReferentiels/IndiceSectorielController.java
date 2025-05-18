package com.projet.administration.Controller.GestionReferentiels;

import com.projet.administration.Entity.GestionReferentiels.IndiceSectoriel;
import com.projet.administration.Service.GestionReferentiels.IndiceSectorielService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.awt.*;
import java.util.List;
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
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;

@RestController
@RequestMapping("/indicesectoriel")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class IndiceSectorielController {
    private final IndiceSectorielService indicesectorielService;


    @Operation(summary = "Get all IndiceSectoriel", description = "Returns a list of all IndiceSectoriel entities")
    @GetMapping("/all")
    public ResponseEntity<List<IndiceSectoriel>> getAllIndiceSectoriel() {
        return ResponseEntity.ok(indicesectorielService.getAllIndiceSectoriel());
    }

    @Operation(summary = "add IndiceSectoriel")
    @PostMapping("/add")
    public ResponseEntity<IndiceSectoriel> addIndiceSectoriel(@RequestBody IndiceSectoriel indiceSectoriel) {
        IndiceSectoriel createdIndiceSectoriel = indicesectorielService.addIndiceSectoriel(indiceSectoriel);
        return new ResponseEntity<>(indiceSectoriel, HttpStatus.CREATED);
    }


    @Operation(summary = "update IndiceSectoriel")
    @PutMapping("/update/{id}")
    public ResponseEntity<IndiceSectoriel> updateIndiceSectoriel(@PathVariable Long id, @RequestBody IndiceSectoriel indiceSectoriel) {
        IndiceSectoriel updatedIndiceSectoriel = indicesectorielService.updateIndiceSectorielWithId(id, indiceSectoriel);
        return updatedIndiceSectoriel != null ? ResponseEntity.ok(updatedIndiceSectoriel) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete IndiceSectoriel")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteIndiceSectoriel(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(indicesectorielService.deleteIndiceSectoriel(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des Indices Sectoriels au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<IndiceSectoriel> list = indicesectorielService.getAllIndiceSectoriel();

        // 2) Générer le PDF en mémoire, A4 paysage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2.a) Logo
        String logoPath = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\src\\assets\\uploads-images\\aaaa.jpeg";
        Image logo = Image.getInstance(logoPath);
        logo.scaleAbsolute(50, 50);
        float pageHeight = document.getPageSize().getHeight();
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

        // 2.c) Titre
        Paragraph titlePara = new Paragraph("Liste des Indices Sectoriels",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau
        PdfPTable table = new PdfPTable(new float[]{3, 3, 4, 3, 3, 3, 3, 5});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = {
                "Code ISIN",
                "Code ICB",
                "Groupe",
                "Mnémonique",
                "Libellé FR",
                "Libellé AR",
                "Libellé EN",
                "Description"
        };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (IndiceSectoriel ind : list) {
            table.addCell(new PdfPCell(new Phrase(ind.getCodeIsin(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getCodeICB(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getGroupe(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getMnemonique(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getLibellear(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(ind.getLibelleen(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(
                    ind.getDescription() != null ? ind.getDescription() : "-", cellFont)));
        }

        document.add(table);
        document.close();

        // 3) Nom de fichier
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "indicesectoriel-" + safeTs + ".pdf";

        // 4) Sauvegarde locale (optionnel)
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\IndiceSectoriel";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoi au client
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }


}
