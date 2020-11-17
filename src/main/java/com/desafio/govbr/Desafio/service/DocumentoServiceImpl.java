package com.desafio.govbr.Desafio.service;


import com.desafio.govbr.Desafio.erro.DocumentoException;
import com.desafio.govbr.Desafio.entity.Documento;
import com.desafio.govbr.Desafio.repository.DocumentoRepository;
import com.desafio.govbr.Desafio.util.StorageStrategy;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DocumentoServiceImpl implements DocumentoService {

    private final Path fileStorageLocation;

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    StorageStrategy storageStrategy;

    @Autowired
    public DocumentoServiceImpl(Documento fileStorageProperties) {

        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            throw new DocumentoException("Não foi possivel o diretorio para armazenar o documento.", ex);
        }

    }

    @Override
    public String upload(MultipartFile file,
                         String tipoDocumento,
                         int localArmazenamento) {

        String nomeArquivo = "";
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getBytes());
            if (detectedType.equals("application/vnd.microsoft.portable-executable") || detectedType.equals("application/bat")) {
                throw new DocumentoException("Tipo de arquivo não suportado.");
            }
            if (localArmazenamento == 1) {
                nomeArquivo = this.salvarArquivo(file, tipoDocumento);
            } else if (localArmazenamento == 2) {
                nomeArquivo = storageStrategy.uploadFile(file);
            } else {
                throw new DocumentoException
                        ("Opções validas para upload -> localArmazenamento 1 - Banco de dados, localArmazenamento 2 - Storage");
            }

        } catch (IOException ex) {
            throw new DocumentoException("Erro ao realizar o upload.");
        }
        return nomeArquivo;
    }

    @Override
    public Resource download(String arquivo) {
        String nomeDoArquivo = this.obterNomeDoArquivo(arquivo);

        Resource resource = null;
        if (nomeDoArquivo != null && !nomeDoArquivo.isEmpty()) {

            try {
                resource = this.obterArquivoAsResource(nomeDoArquivo);
                if (resource == null) {
                    resource = storageStrategy.downloadFile(arquivo);
                }

            } catch (IOException e) {
                throw new DocumentoException("Erro ao realizar o download " + e.getMessage());
            }

            return resource;
        } else {
            return null;
        }
    }


    private String salvarArquivo(MultipartFile file, String tipoDocumento) {

        String nomeOriginalDoArquivo = StringUtils.cleanPath(file.getOriginalFilename());
        String nomeDoArquivo = "";

        try {

            String fileExtension = "";

            try {

                fileExtension = nomeOriginalDoArquivo.substring(nomeOriginalDoArquivo.lastIndexOf("."));

            } catch (Exception e) {

                fileExtension = "";

            }

            nomeDoArquivo = tipoDocumento + fileExtension;


            Path local = this.fileStorageLocation.resolve(nomeDoArquivo);
            Files.copy(file.getInputStream(), local, StandardCopyOption.REPLACE_EXISTING);

            Documento newDoc = new Documento();
            newDoc.setFormatoDocumento(file.getContentType());
            newDoc.setNomeArquivo(nomeDoArquivo);
            newDoc.setTipoDocumento(tipoDocumento);
            documentoRepository.save(newDoc);

            return nomeDoArquivo;

        } catch (IOException ex) {

            throw new DocumentoException("Não foi possivel salvar o arquivo " + nomeDoArquivo + ".", ex);

        }

    }


    private Resource obterArquivoAsResource(String nomeDoArquivo) throws IOException {

        try {

            Path filePath = this.fileStorageLocation.resolve(nomeDoArquivo).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {

                return resource;

            } else {

                return null;

            }

        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Arquivo não encontrado " + nomeDoArquivo);
        }
    }


    private String obterNomeDoArquivo(String arquivo) {
        return documentoRepository.getPathArquivoUpado(arquivo);
    }


}
