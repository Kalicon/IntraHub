package com.example.demo.repository;

import com.example.demo.model.PedidoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoMaterialRepository extends JpaRepository<PedidoMaterial, Long> { // TEM QUE SER PUBLIC
    List<PedidoMaterial> findAllByOrderByDataPedidoDesc();
}