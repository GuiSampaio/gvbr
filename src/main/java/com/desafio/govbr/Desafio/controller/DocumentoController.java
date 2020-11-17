package com.desafio.govbr.Desafio.controller;

import com.desafio.govbr.Desafio.erro.DocumentoException;
import com.desafio.govbr.Desafio.service.DocumentoService;
import com.desafio.govbr.Desafio.service.DocumentoServiceImpl;
import com.desafio.govbr.Desafio.util.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@RestController(value = "/client")
public class DocumentoController {


    private final DocumentoService documentoService;

    @Autowired
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/uploadFile")
    public UploadFileResponse upload(@RequestParam("arquivo") MultipartFile file,
                                     @RequestParam("tipoDoArquivo") String tipoDocumento,
                                     @RequestParam("localArmazenamento") int localArmazenamento,
                                     UriComponentsBuilder uriBuilder) {
        String nomeArquivo = documentoService.upload(file, tipoDocumento, localArmazenamento);
        URI uri = uriBuilder.path("/download/").buildAndExpand(nomeArquivo).toUri();
        return new UploadFileResponse(nomeArquivo, uri.toString(),
                file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> download(@RequestParam("arquivo") String arquivo) {

        Resource resource = documentoService.download(arquivo);
        if (resource != null) {
            String contentType = null;

            try {
                contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
            } catch (IOException ex) {
                throw new DocumentoException("Não foi possível obter o arquivo.");
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
