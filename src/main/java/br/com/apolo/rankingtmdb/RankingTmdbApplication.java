package br.com.apolo.rankingtmdb;

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
        String json = String.valueOf(consumoApi.obterDadosJson("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1"));

        String[] moviesArray = ParseMovies.parseMovies(json);

        List<String> titles = ParseMovies.parseTitles(moviesArray);
        List<String> images = ParseMovies.parseImages(moviesArray);
        List<String> years = ParseMovies.parseYears(moviesArray);
        List<String> ratings = ParseMovies.parseRatings(moviesArray);

        System.out.println("\n\n========= Filmes =========\n\n");
        for (int i = 0; i < titles.size(); i++) {
            System.out.println(titles.get(i) + " (" + years.get(i) + ") - Nota: " + ratings.get(i));
            System.out.println("Imagem: " + images.get(i));
            System.out.println();
        }
    }
}