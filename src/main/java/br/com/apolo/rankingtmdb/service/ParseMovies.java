package br.com.apolo.rankingtmdb.service;

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

        // Limpa chaves sobrando no início/fim
        for (int i = 0; i < moviesArray.length; i++) {
            moviesArray[i] = moviesArray[i].replaceAll("^\\{", "").replaceAll("\\}$", "");
        }

        return moviesArray;
    }

    /**
     * Extrai uma lista de valores de um determinado campo do array de filmes
     */
    private static List<String> parseField(String[] moviesArray, String fieldName) {
        List<String> values = new ArrayList<>();
        for (String movie : moviesArray) {
            int index = movie.indexOf("\"" + fieldName + "\":");
            if (index == -1) continue;

            int valueStart = index + fieldName.length() + 3; // posição logo após o ":"

            // Verifica se começa com aspas (string) ou não (número)
            if (movie.charAt(valueStart) == '\"') {
                int start = valueStart + 1;
                int end = movie.indexOf("\"", start);
                if (end > start) {
                    values.add(movie.substring(start, end));
                } else {
                    values.add("N/A");
                }
            } else {
                // campo numérico: pega até a próxima vírgula ou fim da string
                int end = movie.indexOf(",", valueStart);
                if (end == -1) end = movie.length();
                values.add(movie.substring(valueStart, end).trim());
            }
        }
        return values;
    }

    // Extrai títulos
    public static List<String> parseTitles(String[] moviesArray) {
        return parseField(moviesArray, "title");
    }

    // Extrai poster_path e monta a URL completa
    public static List<String> parseImages(String[] moviesArray) {
        List<String> paths = parseField(moviesArray, "poster_path");
        List<String> urls = new ArrayList<>();
        for (String path : paths) {
            urls.add(IMAGE_BASE_URL + path);
        }
        return urls;
    }

    // Extrai o ano de release_date
    public static List<String> parseYears(String[] moviesArray) {
        List<String> dates = parseField(moviesArray, "release_date");
        List<String> years = new ArrayList<>();
        for (String date : dates) {
            if (date.length() >= 4) {
                years.add(date.substring(0, 4)); // pega os 4 primeiros caracteres
            } else {
                years.add("N/A");
            }
        }
        return years;
    }

    // Extrai vote_average
    public static List<String> parseRatings(String[] moviesArray) {
        return parseField(moviesArray, "vote_average");
    }
}
