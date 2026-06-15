package com.example.demo.repository; // <--- Confira se o pacote é esse mesmo

import com.example.demo.model.Achado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AchadoRepository extends JpaRepository<Achado, Long> { // TEM QUE SER PUBLIC
    List<Achado> findByStatus(String status);
}