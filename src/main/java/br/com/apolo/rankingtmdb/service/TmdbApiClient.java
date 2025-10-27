package br.com.apolo.rankingtmdb.service;

import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


public class TmdbApiClient {

    @Value("${api.tmdb.token}")
    private String token;

    public Optional <String> obterDadosJson(String url) throws URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();

        //Criando o request
        HttpRequest request =  HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        //Criando a response
        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Optional.ofNullable(response.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
