package com.example.demo.repository;
import com.example.demo.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByAtivoTrueOrderByNomeAsc(); // Só traz os ativos em ordem alfabética
}