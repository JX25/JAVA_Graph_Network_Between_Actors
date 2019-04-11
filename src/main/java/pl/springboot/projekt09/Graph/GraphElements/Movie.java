package pl.springboot.projekt09.Graph.GraphElements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jgrapht.graph.DefaultEdge;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie extends DefaultEdge{

    @JsonProperty("id") String id;
    @JsonProperty("title") String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\''+
                '}';
    }

    @JsonCreator
    public Movie( @JsonProperty("id") String id, @JsonProperty("title") String title) {

        this.id = id;
        this.title = title;
    }

}
