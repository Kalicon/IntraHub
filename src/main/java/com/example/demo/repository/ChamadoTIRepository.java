package com.example.demo.repository;
import com.example.demo.model.ChamadoTI;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChamadoTIRepository extends JpaRepository<ChamadoTI, Long> {
    List<ChamadoTI> findAllByOrderByDataAberturaDesc();
}