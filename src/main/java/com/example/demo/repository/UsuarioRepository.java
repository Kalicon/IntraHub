package com.example.demo.repository;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Usuario.
 * Utiliza Spring Data JPA para fornecer operações de CRUD e consultas customizadas.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu login e pelo ID do hospital ao qual pertence.
     * Esta é a consulta fundamental para a segurança do nosso sistema multi-tenant.
     * Ela garante que a busca por um usuário está sempre restrita ao seu próprio hospital.
     *
     * @param login O login do usuário.
     * @param hospitalId O ID do hospital (chave do tenant).
     * @return um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<Usuario> findByLoginAndHospitalId(String login, Long hospitalId);

    Usuario findByLogin(String login);

}