package com.projet.administration.Controller.GestionReferentiels;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.ColumnText;
import com.projet.administration.Entity.GestionReferentiels.TypeMarche;
import com.projet.administration.Service.GestionReferentiels.TypeMarcheService;
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



@RestController
@RequestMapping("/typemarche")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class TypeMarcheController {

    private final TypeMarcheService typeMarcheService;


    @Operation(summary = "Get all TypeMarche", description = "Returns a list of all TypeMarche entities")
    @GetMapping("/all")
    public ResponseEntity<List<TypeMarche>> getAllTypeMarche() {
        return ResponseEntity.ok(typeMarcheService.getAllTypeMarche());
    }

    @Operation(summary = "add TypeMarche")
    @PostMapping("/add")
    public ResponseEntity<TypeMarche> addTypeMarche(@RequestBody TypeMarche typemarche) {
        TypeMarche createdTypeMarche = typeMarcheService.addTypeMarche(typemarche);
        return new ResponseEntity<>(createdTypeMarche, HttpStatus.CREATED);
    }


    @Operation(summary = "update TypeMarche")
    @PutMapping("/update/{id}")
    public ResponseEntity<TypeMarche> updateTypeMarche(@PathVariable Long id, @RequestBody TypeMarche typemarche) {
        TypeMarche updatedTypeMarche = typeMarcheService.updateTypeMarcheWithId(id, typemarche);
        return updatedTypeMarche != null ? ResponseEntity.ok(updatedTypeMarche) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete TypeMarche")
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTypeMarche(@PathVariable (value = "id") Long id) {

        return new ResponseEntity<>(typeMarcheService.deleteTypeMarche(id), HttpStatus.OK);

    }

    @Operation(summary = "Exporter la liste des TypeMarche au format PDF")
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportPdf(HttpServletResponse response) throws Exception {
        // 1) Récupérer les données
        List<TypeMarche> list = typeMarcheService.getAllTypeMarche();

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
        Paragraph titlePara = new Paragraph("Liste des Types de Marché",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)
        );
        titlePara.setAlignment(Paragraph.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);

        // 2.d) Tableau (Code BVMT | SuperType | Libellé FR | Libellé AR | Libellé EN)
        PdfPTable table = new PdfPTable(new float[]{2, 3, 3, 3, 3});
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        String[] headers = { "Code BVMT", "SuperType", "Libellé FR", "Libellé AR", "Libellé EN" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(new Color(41, 128, 185));
            cell.setPadding(6);
            table.addCell(cell);
        }

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (TypeMarche tm : list) {
            table.addCell(new PdfPCell(new Phrase(tm.getCodeBVMT(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(tm.getSuperType(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(tm.getLibellefr(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(tm.getLibellear(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(tm.getLibelleen(), cellFont)));
        }

        document.add(table);
        document.close();

        // 3) Nom de fichier safe Windows
        String safeTs = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss"));
        String fileName = "typemarche-" + safeTs + ".pdf";

        // 4) Sauvegarder sur disque
        String archiveDir = "C:\\Users\\Mon Pc\\Desktop\\PfeFront\\front\\GenerationPDF\\Type Marche";
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
