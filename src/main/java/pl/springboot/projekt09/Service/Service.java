package pl.springboot.projekt09.Service;

import java.util.concurrent.CompletableFuture;

public interface Service {

    public String search(String actorStart, String actorStop);
    public String status(String id);
    public String result(String id);
}
