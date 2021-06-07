package com.example.cinema.web;


import com.example.cinema.dao.FilmRepository;
import com.example.cinema.dao.TicketRepository;
import com.example.cinema.entities.Film;
import com.example.cinema.entities.Ticket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CinemaRestController {
      @Autowired
     private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;

      @GetMapping("/listfilms" )
    public List<Film> getAllFimls()
      {
          return filmRepository.findAll();
      }

      @GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)  // default is Application_json
    public byte[] image(@PathVariable(name = "id") Long id) throws IOException {
          Film f=filmRepository.findById(id).get();
          File file = new File(System.getProperty("user.home")+"/cinema/images/"+f.getPhoto());
          Path path= Paths.get(file.toURI());
          return Files.readAllBytes(path);
      }

      @PostMapping("/payTickets")
      @Transactional
    public List<Ticket> payTickets(@RequestBody TicketForm ticketForm){
          List<Ticket> tickets = new ArrayList<>();
          ticketForm.getTickets().forEach(idTicket->{
              Ticket ticket = ticketRepository.findById(idTicket).get();
              ticket.setReserver(true);
              ticket.setNomClient(ticketForm.getNomClient());
              ticket.setCodePayement(ticketForm.getCodePayment());
              ticketRepository.save(ticket);
              tickets.add(ticket);
          });

          return tickets;
      }



}
@Data
  class TicketForm{

    private String nomClient;
    private Integer codePayment;
    private List<Long> tickets = new ArrayList<>();

  }
