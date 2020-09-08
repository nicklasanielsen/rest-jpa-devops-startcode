package rest;

import com.google.gson.Gson;
import dtos.MovieDTO;
import entities.Movie;
import utils.EMF_Creator;
import facades.MovieFacade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final MovieFacade FACADE = MovieFacade.getMovieFacade(EMF);

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieCount() {
        Long count = FACADE.getMovieCount();

        return "{\"count\":"+count+"}";
    }

    @Path("id/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieByID(@PathParam("id") Long id) {
        Movie movie = FACADE.getMovieByID(id);
        System.out.println(movie);
        return new Gson().toJson(movie);
    }

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMovies() {
        List<Movie> movies = FACADE.getAllMovies();
        List<MovieDTO> movieDTOs = new ArrayList();

        for (Movie movie : movies) {
            movieDTOs.add(new MovieDTO(movie));
        }

        return new Gson().toJson(movieDTOs);
    }

}
