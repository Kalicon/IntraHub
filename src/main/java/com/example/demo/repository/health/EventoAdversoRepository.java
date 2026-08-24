package com.example.demo.repository.health;

import com.example.demo.model.health.EventoAdverso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoAdversoRepository extends JpaRepository<EventoAdverso, Long> {
    List<EventoAdverso> findByStatusAnalise(String statusAnalise);
}