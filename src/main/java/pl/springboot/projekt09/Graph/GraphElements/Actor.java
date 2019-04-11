package pl.springboot.projekt09.Graph.GraphElements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actor{

    @JsonProperty("id") private String id;
    @JsonProperty("name") private String name;

    @JsonCreator
    public Actor(@JsonProperty("id") String id,  @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return (id.equals(actor.getId()));
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
