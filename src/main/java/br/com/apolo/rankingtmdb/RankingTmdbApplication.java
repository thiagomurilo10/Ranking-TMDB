package br.com.apolo.rankingtmdb;

import br.com.apolo.rankingtmdb.service.TmdbApiClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RankingTmdbApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RankingTmdbApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        TmdbApiClient consumoApi = new TmdbApiClient();

        System.out.println(consumoApi.obterDadosJson("https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1"));
    }
}
