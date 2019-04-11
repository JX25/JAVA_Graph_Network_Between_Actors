package pl.springboot.projekt09.Graph;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.springboot.projekt09.Graph.GraphElements.Actor;
import pl.springboot.projekt09.Graph.GraphElements.Movie;
import org.jgrapht.graph.SimpleGraph;
import pl.springboot.projekt09.Service.GraphMovieActorService;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class GraphMovieActor {

    private HashSet<Actor> visitedVertexes;
    private HashSet<Movie> visitedEdges;
    private LinkedList<Actor> queueActors;
    @Autowired
    private GraphMovieActorService graphMovieActorService;
    private Graph<Actor, Movie> g;
    private static final Logger logger = LoggerFactory.getLogger(GraphMovieActor.class);

    @Autowired
    public GraphMovieActor(){
        visitedVertexes = new HashSet<>();
        visitedEdges = new HashSet<>();
        queueActors = new LinkedList<>();
        g = new SimpleGraph<>(Movie.class);
        graphMovieActorService = new GraphMovieActorService(new RestTemplateBuilder());
    }

    public String findTheClosestPathBetweenActors(String begin, String target) throws InterruptedException{
        //graph creation
        try {
            CompletableFuture<Actor> start = graphMovieActorService.actorData(begin);
            CompletableFuture<Actor> stop = graphMovieActorService.actorData(target);
            CompletableFuture.allOf(start,stop).join();
            boolean finishGraph = false;
            queueActors.add(start.get());
            queueActors.addLast(null);
            int i = 1;
            while (!queueActors.isEmpty()) {
                if (finishGraph) break;
                if (i >= 7) return null;
                if (queueActors.getFirst() == null) {
                    i++;
                    queueActors.remove();
                    if (queueActors.isEmpty() || queueActors.getFirst()==null) break;
                }
                Actor actor = queueActors.remove();
                if (visitedVertexes.contains(actor)) continue;
                visitedVertexes.add(actor);
                g.addVertex(actor);
                CompletableFuture<Movie[]> nextEdgesCF = graphMovieActorService.getActorMovies(actor.getId());
                CompletableFuture.allOf(nextEdgesCF).join();
                Movie[] nextEdges = nextEdgesCF.get();
                for (Movie edge : nextEdges) {
                    if(finishGraph) break;
                    if (visitedEdges.contains(edge)) continue;
                    visitedEdges.add(edge);
                    CompletableFuture<Actor[]> actorsCF = graphMovieActorService.getMoviesActors(edge.getId());
                    CompletableFuture.allOf(actorsCF).join();
                    Actor[] actors = actorsCF.get();
                    for (Actor vertex : actors) {
                        //logger.info("in actors");
                        if (visitedVertexes.contains(vertex)) continue;
                        queueActors.addLast(vertex);
                        g.addVertex(vertex);
                        if( !g.containsEdge(actor,vertex) )g.addEdge(actor, vertex, (Movie) edge.clone());
                        if (vertex.equals(stop.get())) {
                            finishGraph = true;
                            break;
                        }
                    }
                }
                queueActors.addLast(null);
            }
            // finding path
            JSONArray path = new JSONArray();
            BellmanFordShortestPath<Actor, Movie> bfsp = new BellmanFordShortestPath<>(g);
            GraphPath<Actor, Movie> shortestPath = bfsp.getPath(start.get(), stop.get());
            List<Movie> movies = shortestPath.getEdgeList();
            List<Actor> actors = shortestPath.getVertexList();
            path.put("Connections:");
            for (int j = 0; j < actors.size(); ++j) {
                if (j == actors.size() - 1)
                    path.put(actors.get(j).getId() + " " + actors.get(j).getName());
                else {
                    path.put(actors.get(j).getId() + " " + actors.get(j).getName());
                    path.put(movies.get(j).getId() + " " + movies.get(j).getTitle());
                }
            }
            return path.toString();
        }catch (Exception e)
        {
            logger.info(begin+" "+target);
            e.printStackTrace();
            return null;
        }
    }
}
