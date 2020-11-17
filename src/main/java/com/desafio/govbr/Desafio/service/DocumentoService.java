package com.desafio.govbr.Desafio.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentoService {

    String upload(MultipartFile file, String tipoDocumento, int localArmazenamento);
    Resource download(String arquivo);
}
