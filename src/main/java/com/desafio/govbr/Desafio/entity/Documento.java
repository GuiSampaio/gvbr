package com.desafio.govbr.Desafio.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;

@Configuration
@ConfigurationProperties(prefix = "file")
@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "fomato_documento")
    private String formatoDocumento;

    @Column(name = "upload_dir")
    private String uploadDir;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFormatoDocumento() {
        return formatoDocumento;
    }

    public void setFormatoDocumento(String formatoDocumento) {
        this.formatoDocumento = formatoDocumento;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
