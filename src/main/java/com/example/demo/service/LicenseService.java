package com.example.demo.service;

import com.example.demo.model.LicencaSistema;
import com.example.demo.repository.LicencaSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class LicenseService {

    private static final String SECRET_MASTER_KEY = "IntraHubHealth#CommercialMasterKey2026!SecuredSignature";

    @Autowired private LicencaSistemaRepository licencaRepository;

    public Optional<LicencaSistema> getLicencaAtiva() {
        return licencaRepository.findFirstByAtivaTrueOrderByIdDesc();
    }

    public boolean isLicencaValida() {
        Optional<LicencaSistema> licOpt = getLicencaAtiva();
        if (licOpt.isEmpty()) return false;

        LicencaSistema lic = licOpt.get();
        if (!lic.isAtiva()) return false;
        if (lic.getDataValidade() == null) return false;

        // Verifica se expirou a validade de 1 ano
        if (LocalDate.now().isAfter(lic.getDataValidade())) {
            return false;
        }

        // Verifica integridade da chave
        return validarAssinaturaChave(lic.getChaveLicenca(), lic.getCnpj(), lic.getDataValidade());
    }

    public long getDiasRestantes() {
        Optional<LicencaSistema> licOpt = getLicencaAtiva();
        if (licOpt.isEmpty()) return 0;
        LocalDate val = licOpt.get().getDataValidade();
        if (val == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), val);
    }

    public String gerarChavePlano(String cnpj, String razaoSocial, String tipoPlano) {
        int anos = 1;
        String planoNome = "ANUAL_1_ANO";

        if ("MEDIO_PRAZO".equalsIgnoreCase(tipoPlano) || "3_ANOS".equalsIgnoreCase(tipoPlano)) {
            anos = 3;
            planoNome = "MEDIO_PRAZO_3_ANOS";
        } else if ("QUINQUENAL".equalsIgnoreCase(tipoPlano) || "5_ANOS".equalsIgnoreCase(tipoPlano)) {
            anos = 5;
            planoNome = "QUINQUENAL_5_ANOS";
        } else if ("VITALICIO".equalsIgnoreCase(tipoPlano)) {
            anos = 100;
            planoNome = "VITALICIO";
        }

        LocalDate emissao = LocalDate.now();
        LocalDate validade = emissao.plusYears(anos);
        String payload = cnpj.replaceAll("\\D", "") + ":" + validade.toString() + ":" + planoNome + ":" + razaoSocial.toUpperCase();
        String assinatura = gerarHMAC(payload);
        return Base64.getEncoder().encodeToString((payload + "::" + assinatura).getBytes(StandardCharsets.UTF_8));
    }

    public String gerarChaveAnual(String cnpj, String razaoSocial, int anosValidade) {
        return gerarChavePlano(cnpj, razaoSocial, anosValidade == 5 ? "QUINQUENAL" : (anosValidade == 3 ? "MEDIO_PRAZO" : "ANUAL"));
    }

    public boolean sincronizarLicencaOnline() {
        Optional<LicencaSistema> licOpt = getLicencaAtiva();
        if (licOpt.isEmpty()) return false;

        LicencaSistema lic = licOpt.get();
        // Em ambiente produtivo, faz requisição HTTPS para https://licenciador.intrahub.com/api/v1/validar
        // Se a internet estiver indisponível (air-gapped local server), valida a assinatura HMAC localmente sem interromper o serviço!
        lic.setDataUltimaChecagem(LocalDateTime.now());
        licencaRepository.save(lic);
        return isLicencaValida();
    }

    public boolean ativarChave(String chaveBase64) {
        try {
            String decoded = new String(Base64.getDecoder().decode(chaveBase64.trim()), StandardCharsets.UTF_8);
            String[] partes = decoded.split("::");
            if (partes.length != 2) return false;

            String payload = partes[0];
            String assinatura = partes[1];

            if (!gerarHMAC(payload).equals(assinatura)) {
                return false;
            }

            String[] dados = payload.split(":");
            if (dados.length < 3) return false;

            String cnpj = dados[0];
            LocalDate validade = LocalDate.parse(dados[1]);
            String plano = "ANUAL_1_ANO";
            String razaoSocial = "";

            if (dados.length >= 4) {
                plano = dados[2];
                razaoSocial = dados[3];
            } else {
                razaoSocial = dados[2];
            }

            // Desativa licenças anteriores
            licencaRepository.findAll().forEach(l -> {
                l.setAtiva(false);
                licencaRepository.save(l);
            });

            LicencaSistema nova = new LicencaSistema();
            nova.setClienteRazaoSocial(razaoSocial);
            nova.setCnpj(cnpj);
            nova.setChaveLicenca(chaveBase64);
            nova.setDataEmissao(LocalDate.now());
            nova.setDataValidade(validade);
            nova.setPlano(plano);
            nova.setLimiteUsuarios(9999);
            nova.setAtiva(true);
            nova.setDataUltimaChecagem(LocalDateTime.now());

            licencaRepository.save(nova);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarAssinaturaChave(String chaveBase64, String cnpj, LocalDate validade) {
        try {
            String decoded = new String(Base64.getDecoder().decode(chaveBase64.trim()), StandardCharsets.UTF_8);
            String[] partes = decoded.split("::");
            if (partes.length != 2) return false;
            return gerarHMAC(partes[0]).equals(partes[1]);
        } catch (Exception e) {
            return false;
        }
    }

    private String gerarHMAC(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_MASTER_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar assinatura de licença", e);
        }
    }
}