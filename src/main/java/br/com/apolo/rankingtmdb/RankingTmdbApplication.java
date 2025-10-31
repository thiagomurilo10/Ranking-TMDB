package br.com.apolo.rankingtmdb;

import br.com.apolo.rankingtmdb.model.Movie;
import br.com.apolo.rankingtmdb.service.ParseMovies;
import br.com.apolo.rankingtmdb.service.TmdbApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RankingTmdbApplication implements CommandLineRunner {

    @Autowired
    private TmdbApiClient consumoApi;

	public static void main(String[] args) {
		SpringApplication.run(RankingTmdbApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        String json = String.valueOf(consumoApi.obterDadosJson(
                "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1"
        ));

        List<Movie> movies = ParseMovies.parseMoviesList(json);

        System.out.println("\n========= 🎬 FILMES TOP RATED =========\n");

        for (Movie movie : movies) {
            System.out.println(movie.titles() + " (" + movie.years() + ") - Nota: " + movie.rating());
            System.out.println("Imagem: " + movie.images());
            System.out.println();
        }
    }
}