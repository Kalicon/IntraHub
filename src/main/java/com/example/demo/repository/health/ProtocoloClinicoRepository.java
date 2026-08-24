package com.example.demo.repository.health;

import com.example.demo.model.health.ProtocoloClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProtocoloClinicoRepository extends JpaRepository<ProtocoloClinico, Long> {
    List<ProtocoloClinico> findByStatus(String status);
    List<ProtocoloClinico> findByTipoProtocolo(String tipoProtocolo);
}