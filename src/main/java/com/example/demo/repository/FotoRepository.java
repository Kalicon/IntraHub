package com.example.demo.repository;

import com.example.demo.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByAlbumId(Long albumId);
}