package facades;

import utils.EMF_Creator;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import entities.Movie;

public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;
    private static List<Movie> movies;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = MovieFacade.getMovieFacade(emf);
        movies = new ArrayList();
    }

    @AfterAll
    public static void tearDownClass() {
        EntityManager em = emf.createEntityManager();

        movies.add(new Movie("Iron Man", "Jon Favreau", 126, 2008));
        movies.add(new Movie("Dum og dummere", "Peter Farrelly", 113, 1994));

        try {
            em.getTransaction().begin();

            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(movies.get(0));
            em.persist(movies.get(1));
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        movies.add(new Movie("Iron Man", "Jon Favreau", 126, 2008));
        movies.add(new Movie("Dum og dummere", "Peter Farrelly", 113, 1994));

        try {
            em.getTransaction().begin();

            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(movies.get(0));
            em.persist(movies.get(1));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        movies.clear();
    }

    @Test
    public void getMovieCountTest() {
        // Arrange
        Long expected = Integer.toUnsignedLong(movies.size());

        // Act
        Long actual = facade.getMovieCount();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getMovieByIDTest_exists() {
        // Arrange
        Movie expected = movies.get(1);

        // Act
        Movie actual = facade.getMovieByID(expected.getId());

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void getMovieByIDTest_doesnt_exists() {
        // Arrange
        Long id = Integer.toUnsignedLong(movies.size() + 1);

        // Act
        Movie actual = facade.getMovieByID(id);

        // Assert
        assertNull(actual);
    }

    @Test
    public void getAllMoviesTest() {
        // Arrange
        List<Movie> expected = movies;

        // Act
        List<Movie> actual = facade.getAllMovies();

        // Assert
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void addMovieTest() {
        // Arrange
        Movie expected = new Movie("Test", "Chris Mason Johnson", 90, 2013);
        
        // Act
        Movie actual = facade.addMovie(expected.getTitle(), expected.getDirector(), expected.getRuntime(), expected.getReleaseYear());
        expected.setId(actual.getId());
        
        // Assert
        assertEquals(expected, actual);
    }

}
