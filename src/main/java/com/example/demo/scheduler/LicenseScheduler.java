package com.example.demo.scheduler;

import com.example.demo.service.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LicenseScheduler {

    private static final Logger log = LoggerFactory.getLogger(LicenseScheduler.class);

    @Autowired
    private LicenseService licenseService;

    /**
     * Executa checagem da licenca a cada 24 horas.
     * Em producao, tenta sincronizar com o servidor central remoto (https://licenciador.intrahub.com).
     * Se o hospital estiver sem internet (air-gapped), a validacao criptografica local
     * garante o funcionamento ininterrupto ate a data de expiracao do contrato.
     */
    @Scheduled(fixedRate = 86400000) // 24 horas em ms
    public void checagemDiariaLicenca() {
        log.info("[IntraHub License] Iniciando checagem programada de licenca...");
        
        boolean valida = licenseService.sincronizarLicencaOnline();
        long dias = licenseService.getDiasRestantes();
        
        if (valida) {
            log.info("[IntraHub License] Licenca VALIDA. Dias restantes: {}", dias);
            
            if (dias <= 30) {
                log.warn("[IntraHub License] ATENCAO: Licenca expira em {} dias! Contate o comercial IntraHub para renovacao.", dias);
            }
        } else {
            log.error("[IntraHub License] LICENCA INVALIDA OU EXPIRADA! O sistema sera bloqueado ate a ativacao de uma nova chave.");
        }
    }
}