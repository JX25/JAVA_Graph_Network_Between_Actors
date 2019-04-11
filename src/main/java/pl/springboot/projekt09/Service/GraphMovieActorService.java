package pl.springboot.projekt09.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.springboot.projekt09.Graph.GraphElements.Actor;
import pl.springboot.projekt09.Graph.GraphElements.Movie;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GraphMovieActorService {

    private static final Logger LOG = LoggerFactory.getLogger(GraphMovieActorService.class);
    final String urlGETActor = "https://java.kisim.eu.org/actors/{id}";
    final String urlGETActorMovies =  "https://java.kisim.eu.org/actors/{id}/movies";
    final String urlGETMovieActors = "https://java.kisim.eu.org/movies/{id}";
    private final RestTemplate restTemplate;

    public GraphMovieActorService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    private LinkedHashMap<String,String> getParams(String id){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("id",id);
        return map;
    }

    @Async("actorMovieSearch")
    public CompletableFuture<Movie[]> getActorMovies(String id) throws IOException, JSONException {
        Map<String,String> params = getParams(id);
        String result = restTemplate.getForObject(urlGETActorMovies,String.class,params);
        JSONArray obj = new JSONArray(result);
        String array = result.toString();
        Movie movies[] = new ObjectMapper().readValue(array,Movie[].class);
        return CompletableFuture.completedFuture(movies);
    }


    @Async("actorMovieSearch")
    public CompletableFuture<Actor[]> getMoviesActors(String id) throws InterruptedException, JSONException, IOException {
        Map<String,String> params = getParams(id);
        String result = restTemplate.getForObject(urlGETMovieActors,String.class,params);
        JSONObject obj = new JSONObject(result);
        String array = obj.getJSONArray("actors").toString();
        Actor actors[] = new ObjectMapper().readValue(array,Actor[].class);
        return CompletableFuture.completedFuture(actors);
    }

    @Async("actorMovieSearch")
    public CompletableFuture<Actor> actorData(String id) throws InterruptedException, IOException {
        Map<String,String> params = getParams(id);
        String result = restTemplate.getForObject(urlGETActor,String.class,params);
        Actor actor = new ObjectMapper().readValue(result,Actor.class);
        return CompletableFuture.completedFuture(actor);
    }
}