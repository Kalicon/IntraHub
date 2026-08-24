package com.example.demo.security;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginBruteForceProtectionService {

    private static final int MAX_TENTATIVAS = 5;
    private static final long TEMPO_BLOQUEIO_MS = TimeUnit.MINUTES.toMillis(15);

    private final ConcurrentHashMap<String, Integer> tentativasPorIp = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> tempoBloqueioPorIp = new ConcurrentHashMap<>();

    public void loginFalhou(String key) {
        if (estaBloqueado(key)) return;

        int tentativas = tentativasPorIp.getOrDefault(key, 0) + 1;
        tentativasPorIp.put(key, tentativas);

        if (tentativas >= MAX_TENTATIVAS) {
            tempoBloqueioPorIp.put(key, System.currentTimeMillis() + TEMPO_BLOQUEIO_MS);
        }
    }

    public void loginSucesso(String key) {
        tentativasPorIp.remove(key);
        tempoBloqueioPorIp.remove(key);
    }

    public boolean estaBloqueado(String key) {
        Long tempoBloqueio = tempoBloqueioPorIp.get(key);
        if (tempoBloqueio == null) return false;

        if (System.currentTimeMillis() > tempoBloqueio) {
            tempoBloqueioPorIp.remove(key);
            tentativasPorIp.remove(key);
            return false;
        }

        return true;
    }
}