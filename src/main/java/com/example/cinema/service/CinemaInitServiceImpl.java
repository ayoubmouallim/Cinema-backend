package com.example.cinema.service;

import com.example.cinema.dao.*;
import com.example.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional  // we can put it here instead of puting it before every function
public class CinemaInitServiceImpl implements ICinemaInitService{

    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private FilmRepository filmRepository;

    @Override
    public void initVilles() {

        Stream.of("Casablanca","Tanger","Agadir").forEach(v->{
            Ville ville = new Ville();
            ville.setName(v);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {

        villeRepository.findAll().forEach(ville->{
            Stream.of("Megarama","Imax","Founoun","Chahrazad").forEach(cinemaName->{
                Cinema cinema = new Cinema();
                cinema.setName(cinemaName);
                cinema.setVille(ville);
                cinema.setNombreSalles(3+(int)(Math.random()*7));
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema->{
           for(int i=0;i<cinema.getNombreSalles();i++)
           {
               Salle salle = new Salle();
               salle.setName("Salle "+(i+1));
               salle.setCinema(cinema);
               salle.setNombrePlaces(20+(int)(Math.random()*10));
               salleRepository.save(salle);
           }
        });
    }

    @Override
    public void initSeances() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("10:00","15:00","17:00").forEach(heure->{
            Seance seance =  new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(heure));
                seanceRepository.save(seance);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(salle-> {
            for (int i = 0; i < salle.getNombrePlaces(); i++) {
                Place place = new Place();
                place.setNumeroPlace(i+1);
                place.setSalle(salle);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initFilms() {
        double []durees = new double[]{1,1.5,2,2.5,3};
        List<Categorie> categories =  categorieRepository.findAll();
        Stream.of("Game of Thrones","Taken","Spider Man","Iron Man").forEach(titre->{
         Film film = new Film();
         film.setTitre(titre);
         film.setDuree(durees[new Random().nextInt(durees.length)]);
         film.setPhoto(titre.replace(" ","")+".jpg");
         film.setCategorie(categories.get(new Random().nextInt(categories.size())));
        filmRepository.save(film);
        });

    }

    @Override
    public void initTickets() {

        projectionRepository.findAll().forEach(projection -> {
            projection.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(projection.getPrix());
                ticket.setProjection(projection);
                ticket.setReserver(false);
                ticketRepository.save(ticket);
            });
        });

    }

    @Override
    public void initProjections() {
        double []prix = new double[]{40,50,60,70,80,90};
        List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville->{
            ville.getCinemas().forEach(cinema->{
                cinema.getSalles().forEach(salle->{
                    int i = new Random().nextInt(films.size());
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setSeance(seance);
                            projection.setFilm(films.get(i));
                            projection.setSalle(salle);
                            projection.setDateProjection(new Date());
                            projection.setPrix(prix[new Random().nextInt(prix.length)]);

                            projectionRepository.save(projection);
                        });
                    });
                });
            });


    }

    @Override
    public void initCategories() {
        Stream.of("Action","Drama","Fiction","Histoire").forEach(name->{
            Categorie categorie = new Categorie();
                categorie.setName(name);
                categorieRepository.save(categorie);
        });
    }
}
