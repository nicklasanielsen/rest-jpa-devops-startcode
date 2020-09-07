package facades;

import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Long getMovieCount() {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createQuery("SELECT COUNT(m) FROM Movie m");

            return (Long) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public Movie getMovieByID(Long ID) {
        EntityManager em = getEntityManager();

        try {
            Movie movie = em.find(Movie.class, ID);

            return movie;
        } finally {
            em.close();
        }
    }

    public List<Movie> getAllMovies() {
        EntityManager em = getEntityManager();

        try {
            Query query = em.createQuery("SELECT m FROM Movie m");

            List<Movie> movies = query.getResultList();
            return movies;
        } finally {
            em.close();
        }
    }

    public Movie addMovie(String title, String director, int runtime, int releaseYear) {
        EntityManager em = getEntityManager();

        Movie movie = new Movie(title, director, runtime, releaseYear);

        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();

            return movie;
        } finally {
            em.close();
        }
    }

}
