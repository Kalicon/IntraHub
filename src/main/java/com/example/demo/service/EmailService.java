package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // E-mail que receberá os chamados e solicitações administrativas
    @Value("${app.mail.admin-destination:admin@corphub.com}")
    private String emailDestinoAdmin;

    // E-mail que aparecerá como remetente
    @Value("${spring.mail.username:naorespondahmlmb@gmail.com}")
    private String remetente;

    // --- 1. NOTIFICAÇÃO DE TI ---
    public void notificarTI(String solicitante, String ramal, String titulo, String descricao) {
        String assunto = "[TI] Novo Chamado: " + titulo;
        String corpo = "Solicitante: " + solicitante + "\n" +
                "Ramal: " + ramal + "\n\n" +
                "Assunto: " + titulo + "\n" +
                "Detalhes: " + descricao;

        enviarEmail(emailDestinoAdmin, assunto, corpo);
    }

    // --- 2. NOTIFICAÇÃO DE MANUTENÇÃO ---
    public void notificarManutencao(String solicitante, String setor, String tipo, String descricao) {
        String assunto = "[MANUTENÇÃO] " + tipo;
        String corpo = "Solicitante: " + solicitante + "\n" +
                "Setor: " + setor + "\n" +
                "Tipo: " + tipo + "\n\n" +
                "Descrição: " + descricao;

        enviarEmail(emailDestinoAdmin, assunto, corpo);
    }

    // --- 3. NOTIFICAÇÃO DE RESERVA DE SALA ---
    public void notificarSolicitacaoReserva(String solicitante, String ramal, String sala, String data, String motivo) {
        String assunto = "📅 Solicitação de Sala: " + sala;
        String corpo = "Uma nova reserva foi solicitada.\n\n" +
                "Solicitante: " + solicitante + "\n" +
                "Ramal: " + ramal + "\n" +
                "Sala: " + sala + "\n" +
                "Data/Hora: " + data + "\n" +
                "Motivo: " + motivo + "\n\n" +
                "Por favor, verifique a disponibilidade no sistema.";

        enviarEmail(emailDestinoAdmin, assunto, corpo);
    }

    // --- 4. CONFIRMAÇÃO DE INSCRIÇÃO ---
    public void enviarConfirmacaoInscricao(String emailParticipante, String nomeEvento, String dataEvento) {
        if (emailParticipante == null || emailParticipante.trim().isEmpty()) return;

        String assunto = "✅ Inscrição Confirmada: " + nomeEvento;
        String corpo = "Olá,\n\n" +
                "Sua inscrição para o evento '" + nomeEvento + "' foi confirmada com sucesso.\n" +
                "Data do Evento: " + dataEvento + "\n\n" +
                "Contamos com sua presença!";

        enviarEmail(emailParticipante, assunto, corpo);
    }

    // --- 5. LEMBRETE DE EVENTO (O QUE ESTAVA FALTANDO) ---
    @Async
    public void enviarLembreteEvento(String emailParticipante, String nomeEvento) {
        if (emailParticipante == null || emailParticipante.trim().isEmpty()) return;

        String assunto = "⏰ Lembrete: Evento HOJE - " + nomeEvento;
        String corpo = "Olá,\n\n" +
                "Lembrete: O evento '" + nomeEvento + "' acontece hoje!\n\n" +
                "Não se atrase.";

        enviarEmail(emailParticipante, assunto, corpo);
    }

    // --- MÉTODO ASSÍNCRONO GENÉRICO ---
    @Async
    public void enviarEmail(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);

            mailSender.send(message);
            System.out.println("Async E-mail enviado para: " + para);

        } catch (Exception e) {
            System.err.println("ERRO AO ENVIAR E-MAIL: " + e.getMessage());
        }
    }
}