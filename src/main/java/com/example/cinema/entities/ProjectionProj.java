package com.example.cinema.entities;

import org.springframework.data.rest.core.config.Projection;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Date;

@Projection(name = "p1", types = {com.example.cinema.entities.Projection.class})
public interface ProjectionProj {

    public Long getId();
    public Date getDateProjection();
    public Double getPrix();
    public Salle getSalle();
    public Film getFilm();
    public Collection<Ticket> getTickets();
    public Seance getSeance();

}
