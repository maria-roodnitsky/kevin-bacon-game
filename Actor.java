import java.util.Comparator;

/* Author: Maria Roodnitsky and Chris Wright
   Date: 11/5/2019
   Function: Class of comparable actor objects
 */

public class Actor implements Comparable<Actor>{
    String name;
    int costars;
    double degreeofseperation;

    //constructor
    public Actor(String name, int costars, double degreeofseperation) {
        this.name = name;
        this.costars = costars;
        this.degreeofseperation = degreeofseperation;
    }

    //compare function
    public int compareTo(Actor a2) {
        if (this.degreeofseperation - a2.degreeofseperation > 0) {
            return 1;
        } else if (this.degreeofseperation - a2.degreeofseperation < 0) {
            return -1;
        } else return 0;
    }

    @Override
    public String toString() {
        return name + " has " + costars + " costars and an average degree of separation of " + degreeofseperation;
    }
}
