package com.example.demo.repository;

import com.example.demo.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GaleriaRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByOrderByDataCriacaoDesc();
}