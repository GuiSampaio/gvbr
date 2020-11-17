package com.desafio.govbr.Desafio.repository;

import com.desafio.govbr.Desafio.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Query("Select a.nomeArquivo from Documento a where a.nomeArquivo = ?1")
    String getPathArquivoUpado(String arquivo);

}
