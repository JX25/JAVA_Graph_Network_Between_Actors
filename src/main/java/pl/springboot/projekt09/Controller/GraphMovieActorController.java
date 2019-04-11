package pl.springboot.projekt09.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import pl.springboot.projekt09.Graph.GraphMovieActor;

import java.util.concurrent.CompletableFuture;

@Service
public class GraphMovieActorController {
    @Async("mainExecutor")
    public CompletableFuture<String> find(String start, String stop) throws InterruptedException {
        GraphMovieActor graph = new GraphMovieActor();
        return CompletableFuture.completedFuture( graph.findTheClosestPathBetweenActors(start,stop) );
    }
}
