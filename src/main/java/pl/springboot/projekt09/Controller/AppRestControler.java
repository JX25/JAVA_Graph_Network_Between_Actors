package pl.springboot.projekt09.Controller;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.springboot.projekt09.Graph.GraphMovieActor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/actors")
public class AppRestControler {

    private static final Logger logger = LoggerFactory.getLogger(AppRestControler.class);
    @Autowired
    private  GraphMovieActorController graphMovieActorController;
    private Map<String,CompletableFuture<String>> results = new HashMap<>();
    private static int i = 0;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Integer> findPathBetweenActors(@RequestParam("destination") String destination,
                                                         @RequestParam("target") String target) throws InterruptedException {
        results.put(String.valueOf(i),graphMovieActorController.find(destination,target));
        i++;
        return new ResponseEntity<>(i-1, HttpStatus.OK);
    }

    @RequestMapping(value = "/status{id}",method = RequestMethod.GET)
    public ResponseEntity<String> findPathBetweenActorsStatus(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        if( results.get(id).isDone())
        {
            return new ResponseEntity<>("Task "+id+" is finished",HttpStatus.OK);
        }
        else return new ResponseEntity<>("Task "+id+" still processing",HttpStatus.OK);
    }

    @RequestMapping(value = "/result{id}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> findPathBetweenActorsResult(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        if( results.get(id).isDone())
        {
            return new ResponseEntity<>(results.get(id).get(),HttpStatus.OK);
        }
        else  return new ResponseEntity<>("Searching task id: "+id+" still processing",HttpStatus.OK);
    }

}
