package com.example.demo.scheduler;

import com.example.demo.model.Evento;
import com.example.demo.model.Inscricao;
import com.example.demo.repository.EventoRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EventoScheduler {

    @Autowired private EventoRepository eventoRepository;
    @Autowired private EmailService emailService;

    // Roda todo dia as 7 da manhã
    @Scheduled(cron = "0 0 7 * * *")
    public void enviarLembretes() {
        System.out.println(">>> Verificando eventos de hoje...");
        List<Evento> eventos = eventoRepository.findAll();
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        for (Evento ev : eventos) {
            if (ev.getDataEvento() != null && ev.getDataEvento().equals(hoje)) {
                System.out.println(">>> Evento HOJE: " + ev.getTitulo());
                for (Inscricao i : ev.getInscricoes()) {
                    emailService.enviarLembreteEvento(i.getEmail(), ev.getTitulo());
                }
            }
        }
    }
}