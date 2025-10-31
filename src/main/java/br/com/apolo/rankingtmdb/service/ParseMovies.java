package br.com.apolo.rankingtmdb.service;

import br.com.apolo.rankingtmdb.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class ParseMovies {

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    // Extrai o array de filmes da resposta JSON
    public static String[] parseMovies(String json) {
        int resultsIndex = json.indexOf("\"results\":");
        if (resultsIndex == -1) {
            System.out.println("JSON inválido — não contém o campo 'results'");
            return new String[0];
        }

        int start = json.indexOf("[", resultsIndex);
        int end = json.lastIndexOf("]");
        if (start == -1 || end == -1) {
            System.out.println("JSON inválido — não contém colchetes [] para 'results'");
            return new String[0];
        }

        String moviesJson = json.substring(start + 1, end);
        String[] moviesArray = moviesJson.split("\\},\\{");

        for (int i = 0; i < moviesArray.length; i++) {
            moviesArray[i] = moviesArray[i].replaceAll("^\\{", "").replaceAll("\\}$", "");
        }

        return moviesArray;
    }

    //Metodo genérico para extrair um campo

    private static List<String> parseField(String[] moviesArray, String fieldName) {
        List<String> values = new ArrayList<>();
        for (String movie : moviesArray) {
            int index = movie.indexOf("\"" + fieldName + "\":");
            if (index == -1) continue;

            int valueStart = index + fieldName.length() + 3;

            if (movie.charAt(valueStart) == '\"') {
                int start = valueStart + 1;
                int end = movie.indexOf("\"", start);
                if (end > start) {
                    values.add(movie.substring(start, end));
                } else {
                    values.add("N/A");
                }
            } else {
                int end = movie.indexOf(",", valueStart);
                if (end == -1) end = movie.length();
                values.add(movie.substring(valueStart, end).trim());
            }
        }
        return values;
    }

    //Novo metodo: retorna uma lista de objetos Movie

    public static List<Movie> parseMoviesList(String json) {
        List<Movie> movies = new ArrayList<>();

        String[] moviesArray = parseMovies(json);
        List<String> titles = parseField(moviesArray, "title");
        List<String> paths = parseField(moviesArray, "poster_path");
        List<String> ratings = parseField(moviesArray, "vote_average");
        List<String> dates = parseField(moviesArray, "release_date");

        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            String path = (i < paths.size()) ? paths.get(i) : "";
            String rating = (i < ratings.size()) ? ratings.get(i) : "N/A";
            String date = (i < dates.size()) ? dates.get(i) : "N/A";

            String urlImage = IMAGE_BASE_URL + path;
            String year = (date.length() >= 4) ? date.substring(0, 4) : "N/A";

            movies.add(new Movie(title, urlImage, rating, year));
        }

        return movies;
    }
}
