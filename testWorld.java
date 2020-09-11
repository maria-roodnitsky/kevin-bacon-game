import java.util.ArrayList;
import java.util.Set;

/* Author: Maria Roodnitsky and Chris Wright
   Date: 11/5/2019
   Function: Class to test the functions within GraphMapLibrary
 */

public class testWorld {
    public static String actorsTest = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\actorsTest.txt";
    public static String moviesTest = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\moviesTest.txt";
    public static String movieActorsTest = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\movie-actorsTest.txt";

    public testWorld(){}

    public static void main(String[] args) {
        //working world
        AdjacencyMapGraph<String, String> hollywood = WorldCreator.createWorld(actorsTest, moviesTest, movieActorsTest);

        //empty world
        AdjacencyMapGraph<String, String> bollywood = new AdjacencyMapGraph<>();

        //world with one element
        AdjacencyMapGraph<String, String> lonelywood = new AdjacencyMapGraph<>();
        lonelywood.insertVertex("Lonely");

        /* bfs testing */

            //test bfs on an existing vertex in an existing world
            AdjacencyMapGraph<String, String> baconworld = GraphMapLibrary.bfs(hollywood, "Kevin Bacon");
            AdjacencyMapGraph<String, String> nobodyworld = GraphMapLibrary.bfs(hollywood, "Nobody");

            System.out.println("bfs on bacon: " + baconworld);
            System.out.println("bfs on nobody: " + nobodyworld);

            //boundary case: test bfs on a non-existent vertex
            AdjacencyMapGraph<String, String> watsonworld = GraphMapLibrary.bfs(hollywood, "Emma Watson");
            System.out.println("bfs on watson: " + watsonworld);

            //boundary case: test bfs on a world that doesn't exist (bollywood)
            AdjacencyMapGraph<String, String> baconinbollywood = GraphMapLibrary.bfs(bollywood, "Kevin Bacon");
            System.out.println("bfs on bacon in bollywood : " + baconinbollywood);

            //boundary case: test bfs on a world with one vertex
            AdjacencyMapGraph<String, String> lonelyworld = GraphMapLibrary.bfs(lonelywood, "Lonely");
            System.out.println("bfs on lonely:" + lonelyworld);

        /* missing vertice testing */

            //test missing vertices on a valid graph and subgraph
            Set<String> notinbacon = GraphMapLibrary.missingVertices(hollywood, baconworld);
            Set<String> notinnobody = GraphMapLibrary.missingVertices(hollywood, nobodyworld);
            Set<String> notinwatson = GraphMapLibrary.missingVertices(hollywood, watsonworld);
            Set<String> notinlonely = GraphMapLibrary.missingVertices(lonelywood, lonelyworld);

            System.out.println("\n\nmissing vertices in bacon: " + notinbacon);
            System.out.println("missing vertices in nobody: " + notinnobody);
            System.out.println("missing vertices in watson: "+ notinwatson);
            System.out.println("missing vertices in lonely" + notinlonely);

            //boundary case: test missing vertices on a valid subgraph but not graph
            Set<String> notwithbaconinbollywood = GraphMapLibrary.missingVertices(bollywood, baconworld);
            System.out.println("missing bacon world vertices in bollywood : " + notwithbaconinbollywood);

        /* average distance testing */

            //test good graph
            double distancefromBacon = GraphMapLibrary.averageSeparation(baconworld, "Kevin Bacon");
            System.out.println("\n\naverage distance from Bacon: " + distancefromBacon);

            //add a vertex to change the average path length
            hollywood.insertVertex("Maria Roodnitsky");
            hollywood.insertDirected("Maria Roodnitsky", "Alice", "Maria appeared in Alice's Day with Alice");
            hollywood.insertDirected("Alice", "Maria Roodnitsky", "Alice appeared in Alice's Day with Maria Roodnitsky");

            double adjdistancefromBacon =
                    GraphMapLibrary.averageSeparation(GraphMapLibrary.bfs(hollywood, "Kevin Bacon"), "Kevin Bacon");
            System.out.println("new average distance from Bacon :" + adjdistancefromBacon);

            double distancefromNobody = GraphMapLibrary.averageSeparation(nobodyworld, "Nobody");
            System.out.println("average distance from Nobody: " + distancefromNobody);

            //boundary case: test empty graph (should return -1)
            double distancefromWatson = GraphMapLibrary.averageSeparation(watsonworld, "Emma Watson");
            System.out.println("average distance from Watson: "+ distancefromWatson);

            //boundary case: test graph with one vertex (should return 0)
            double distancefromLonely = GraphMapLibrary.averageSeparation(lonelyworld, "Lonely");
            System.out.println("average distance from Lonely: "+ distancefromLonely);

        /* test path sum finder*/

            //test finding a path that should exist
            ArrayList<String> path = GraphMapLibrary.getPath(hollywood, "Kevin Bacon", "Maria Roodnitsky");
            if (!path.isEmpty()) {
                System.out.println("\nMaria Roodnitsky's Kevin Bacon number is " + (path.size() - 1) + "\n");
                for (int i = 0; i < path.size() - 1; i++) {
                    System.out.println(hollywood.getLabel(path.get(i), path.get(i+1)));
                }
            }
            else {
                System.out.println("\nThere is no path from Maria Roodnitsky to Kevin Bacon.\n");
            }

            //boundary case: test finding a non-existent path from an existing vertex
            path = GraphMapLibrary.getPath(hollywood, "Nobody", "Emma Watson");
            if (!path.isEmpty()) {
                System.out.println("\nEmma Watson's Nobody number is " + (path.size() - 1) + "\n");
                for (int i = 0; i < path.size() - 1; i++) {
                    System.out.println(hollywood.getLabel(path.get(i), path.get(i+1)));
                }
            }
            else {
                System.out.println("\nThere is no path from Emma Watson to Nobody.\n");
            }

            //boundary case: test finding a non-existent path from an non-existing vertex
            path = GraphMapLibrary.getPath(hollywood, "Emma Watson", "Maria Roodnitsky");
            if (!path.isEmpty()) {
                System.out.println("\nMaria Roodnitsky's Emma Watson number is " + (path.size() - 1) + "\n");
                for (int i = 0; i < path.size() - 1; i++) {
                    System.out.println(hollywood.getLabel(path.get(i), path.get(i+1)));
                }
            }
            else {
                System.out.println("\nThere is no path from Maria Roodnitsky to Emma Watson.\n");
            }
        System.out.println("\n\nTesting complete!");
    }
}
