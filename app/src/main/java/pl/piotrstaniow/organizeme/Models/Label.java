package pl.piotrstaniow.organizeme.Models;
import java.io.Serializable;

/**
 * Created by oszka on 25.05.15.
 */
public class Label implements Serializable {
    private String name;
    public Label(String name){
        this.name=name;
    }
    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
