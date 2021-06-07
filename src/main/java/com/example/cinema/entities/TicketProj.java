package com.example.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Projection(name = "p2",types = {Ticket.class})
public interface TicketProj {

    public Long getId();
    public  String getNomClient();
    public Double getPrix();
    public Integer getCodePayement();
    public Boolean getReserver();
    public Place getPlace();
}
