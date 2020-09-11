import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/* Author: Maria Roodnitsky and Chris Wright
   Date: 11/5/2019
   Function: Creates a world (graph of connected actors) given an actor, movie, and movieActor file
 */
public class WorldCreator {

    public WorldCreator(){

    }

    /* creates the Hollywood world*/
    public static AdjacencyMapGraph<String, String> createWorld(String actorFile, String movieFile, String movieActorsFile) {

        AdjacencyMapGraph<String, String> hollywood = new AdjacencyMapGraph<>();

        //data maps (first part of name is key; second is value)
        Map<String, String> idActorNameMap = new HashMap<String, String>();
        Map<String, String> idMovieNameMap = new HashMap<String, String>();
        Map<String, LinkedList<String>> actorMovieMap = new HashMap<String, LinkedList<String>>();
        Map<String, LinkedList<String>> movieActorMap = new HashMap<String, LinkedList<String>>();

        try {
            // Build ID-ActorName map
            BufferedReader input = new BufferedReader(new FileReader(actorFile));
            String actorInformation = null;

            while ((actorInformation = input.readLine()) != null) {
                String[] split = actorInformation.split("\\|");
                idActorNameMap.put(split[0], split[1]);
            }

            input.close();

            // Build ID-MovieName map
            input = new BufferedReader(new FileReader(movieFile));
            String movieInformation = null;

            while ((movieInformation = input.readLine()) != null) {
                String[] split = movieInformation.split("\\|");
                idMovieNameMap.put(split[0], split[1]);
            }

            input.close();

            // Build Movie to Actor and Actor to Movie Maps
            input = new BufferedReader(new FileReader(movieActorsFile));
            String movieActorInformation = null;

            while ((movieActorInformation = input.readLine()) != null) {
                String[] split = movieActorInformation.split("\\|");

                LinkedList<String> movielist = new LinkedList<String>();
                LinkedList<String> actorlist = new LinkedList<String>();

                //if the actor already exists, obtain their list of movies
                if (actorMovieMap.containsKey(idActorNameMap.get(split[1]))) {
                    movielist = actorMovieMap.get(idActorNameMap.get(split[1]));
                }
                //if the movie already exists, obtain its list of actors
                if (movieActorMap.containsKey(idMovieNameMap.get(split[0]))) {
                    actorlist = movieActorMap.get(idMovieNameMap.get(split[0]));
                }
                //add the movie to the list of movies an actor has appeared in
                movielist.add(0, idMovieNameMap.get(split[0]));
                actorMovieMap.put(idActorNameMap.get(split[1]), movielist);
                //add the actor to the list of costars in the movie
                actorlist.add(0, idActorNameMap.get(split[1]));
                movieActorMap.put(idMovieNameMap.get(split[0]), actorlist);
            }

            input.close();


            //create vertices for every actor in Hollywood
            for (String actor : actorMovieMap.keySet()) hollywood.insertVertex(actor);

            //create the edges between the actors
            for (String actor : actorMovieMap.keySet()) {
                for (String movie : actorMovieMap.get(actor)) {
                    for (String costar : movieActorMap.get(movie)) {
                        if (costar.equals(actor)) continue;
                        hollywood.insertDirected(costar, actor, costar + " appeared in " + movie + " with " + actor);
                        hollywood.insertDirected(actor, costar, actor + " appeared in " + movie + " with " + costar);
                    }
                }
            }

            //return the graph of the world
            return hollywood;

        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        } catch (Exception e) {
            System.out.println("Unknown Exception");
        }
        finally {
            //if things fail, a null version should be returned
            return hollywood;
        }
    }
}
