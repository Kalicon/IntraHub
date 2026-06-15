package com.example.demo.repository;

import com.example.demo.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Busca documentos filtrando pela pasta (ex: só enfermagem)
    List<Documento> findByPastaOrderByDataUploadDesc(String pasta);
}