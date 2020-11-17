package com.desafio.govbr.Desafio.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageStrategy {

    String uploadFile(MultipartFile multipartFile) throws IOException;
    ByteArrayResource downloadFile(String fileName) throws IOException;
}
