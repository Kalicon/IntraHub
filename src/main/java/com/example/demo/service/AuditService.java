package com.example.demo.service;

import com.example.demo.model.AuditLog;
import com.example.demo.model.Usuario;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável por gerenciar os logs de auditoria.
 */
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Grava um registro de auditoria no banco de dados.
     * Esta operação é executada em uma nova transação para garantir que o log
     * seja salvo mesmo que a transação principal falhe após este ponto.
     *
     * @param log O objeto AuditLog a ser persistido.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void gravarLog(AuditLog log) {
        auditLogRepository.save(log);
    }

    /**
     * Registra uma ação do usuário no log de auditoria.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String acao, String modulo, String detalhes) {
        String username = "Sistema";
        Long usuarioId = null;
        Long hospitalId = 1L;

        try {
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                username = auth.getName();
                Usuario u = usuarioRepository.findByLogin(username);
                if (u != null) {
                    usuarioId = u.getId();
                    hospitalId = u.getHospitalId();
                }
            }
        } catch (Exception e) {
            // Ignora erros ao obter contexto
        }

        AuditLog log = new AuditLog();
        log.setHospitalId(hospitalId);
        log.setUsuarioId(usuarioId);
        log.setUsuario(username);
        log.setAcao(acao);
        log.setEntidade(modulo);
        log.setValorNovo(detalhes);
        
        auditLogRepository.save(log);
    }
}