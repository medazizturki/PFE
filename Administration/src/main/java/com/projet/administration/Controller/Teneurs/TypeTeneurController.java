package com.projet.administration.Controller.Teneurs;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.projet.administration.Entity.Teneur.TypeTeneur;
import com.projet.administration.Service.Teneurs.TypeTeneurService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lowagie.text.Document;
import java.awt.*;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    @Operation(summary = "Exporter la liste des TypesTeneur au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<TypeTeneur> list = typeTeneurService.getAllTypeTeneur();

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
        Paragraph titlePara = new Paragraph("Liste des Types Teneur",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau
        PdfPTable table = new PdfPTable(new float[]{3, 7});
        table.setWidthPercentage(100);
        // en-têtes
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell h1 = new PdfPCell(new Phrase("Code", headFont));
        h1.setBackgroundColor(new Color(41, 128, 185));
        h1.setPadding(6);
        PdfPCell h2 = new PdfPCell(new Phrase("Libellé", headFont));
        h2.setBackgroundColor(new Color(41, 128, 185));
        h2.setPadding(6);
        table.addCell(h1);
        table.addCell(h2);
        // lignes
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (TypeTeneur t : list) {
            table.addCell(new PdfPCell(new Phrase(t.getCode(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(t.getLibelle(), cellFont)));
        }
        document.add(table);

        document.close();

        // 3) Préparer un nom de fichier validé sous Windows
        DateTimeFormatter fileNameFmt = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        String rawTimestamp  = LocalDateTime.now().format(fileNameFmt);
        String safeTimestamp = rawTimestamp.replace("/", "-").replace(":", ".");
        String fileName      = "types-teneur-" + safeTimestamp + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Type Teneur";
        File dir = new File(archiveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Impossible de créer le dossier : " + archiveDir);
        }
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            baos.writeTo(fos);
        }

        // 5) Envoyer au client pour téléchargement
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(baos.size());
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        response.flushBuffer();
    }
}
