package com.example.demo.repository;

import com.example.demo.model.ReservaFrota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaFrotaRepository extends JpaRepository<ReservaFrota, Long> { // TEM QUE SER PUBLIC
}