import java.util.*;

/* Author: Maria Roodnitsky and Chris Wright
   Date: 11/5/2019
   Function: Creates an interactive user-interface for the game
 */


public class useWorld {
    public static String actors = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\actors.txt";
    public static String movies = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\movies.txt";
    public static String movieActors = "C:\\Users\\Maria\\IdeaProjects\\CS10\\ps4\\movie-actors.txt";

    private static String lead = "Kevin Bacon";

    public useWorld(){}

    public static void main(String[] args) {
        //instantiate a Scanner
        Scanner in = new Scanner(System.in);

        //create a "game on" boolean that can be toggled "off" by a user prompt
        boolean gameplay = true;

        //create the full Hollywood universe
        AdjacencyMapGraph<String, String> hollywood = WorldCreator.createWorld(actors, movies, movieActors);

        while (gameplay) {
            //basic menu
            System.out.print("\n\nWelcome to the " + lead + " game! What would you like to do? \n" +
                    "Type 'play' to play the game with " + lead + " as the center of the universe. \n" +
                    "Type 'change actor' to change the center of the universe. \n" +
                    "Type 'best worlds' to find the best Bacon world alternatives.\n" +
                    "Type 'quit' to quit. \n>");

            //store the initial choice
            String choice = in.nextLine();

            if (choice.equals("change actor")) {
                System.out.print("Who should be the center of the universe?\n>");
                String leadAudition = in.nextLine();
                if (hollywood.hasVertex(leadAudition)) lead = leadAudition;
                else System.out.println(leadAudition + " does not exist in this universe! Returning to " + lead +
                        " game.\n");
            }

            else if (choice.equals("play")) {
                System.out.print("What should we do? \n" +
                        "Type 'average separation' to find the average number of degrees of separation " +
                        "in this world.\n" +
                        "Type 'find path' to find a path to another actor. \n" +
                        "Type 'world statistics' to see how many actors are connected to " + lead +
                        " and how many are not.\n>");

                //do a bfs to prepare the world for analysis
                AdjacencyMapGraph<String, String> world = GraphMapLibrary.bfs(hollywood, lead);

                //store the next choice
                String playChoice = in.nextLine();

                if (playChoice.equals("average separation")){
                    double averagePath = GraphMapLibrary.averageSeparation(world, lead);
                    System.out.print("The average number of degrees of separation in the " + lead + " world is "
                            + averagePath +".\n" );
                }
                else if (playChoice.equals("find path")){
                    System.out.println("To whom should we find a path?\n>");
                    String costarAudition = in.nextLine();
                    String costar;
                    //check to make sure the person exists
                    if (!hollywood.hasVertex(costarAudition)) {
                        System.out.println(costarAudition + " does not exist in this universe! Returning to "
                                + lead + " game.\n");
                    }
                    else {
                        costar = costarAudition;
                        ArrayList<String> path = GraphMapLibrary.getPath(hollywood, lead, costar);
                        if (!path.isEmpty()) {
                            System.out.println(costar + "'s " + lead + " number is " + (path.size() - 1) + "\n");
                            for (int i = 0; i < path.size() - 1; i++) {
                                System.out.println(world.getLabel(path.get(i), path.get(i+1)));
                            }
                        }
                        else {
                            System.out.println("There is no path from " + costar + " to " + lead + ".\n");
                        }
                    }
                }
                else if (playChoice.equals("world statistics")){
                    Set<String> missingActors = GraphMapLibrary.missingVertices(hollywood, world);
                    System.out.println("There are " + hollywood.numVertices() + " actors in the world.");
                    System.out.println("Of these, " + (hollywood.numVertices() - missingActors.size()) +
                            " can be traced to a movie with " + lead + ".");
                    System.out.println(missingActors.size() + " cannot.\n");
                }
                else {
                    System.out.println("Invalid choice.");
                }

            }
            else if (choice.equals("best worlds")){
                PriorityQueue<Actor> costarsQueue = new PriorityQueue<Actor>((Actor a1, Actor a2) ->
                        a2.costars - a1.costars);
                PriorityQueue<Actor> separationQueue = new PriorityQueue<Actor>();

                for (String actor: hollywood.vertices()){
                    AdjacencyMapGraph<String, String> actorworld = GraphMapLibrary.bfs(hollywood, actor);
                    double averagePathToActor = GraphMapLibrary.averageSeparation(actorworld, actor);
                    int costarCount = hollywood.inDegree(actor);

                    costarsQueue.add(new Actor(actor, costarCount, averagePathToActor));
                    //an actor will only be good if they are connected to most of the other actors
                    if (actorworld.numVertices() > 4000) {
                        separationQueue.add(new Actor(actor, costarCount, averagePathToActor));
                    }
                }

                ArrayList<Actor> top10Costar = new ArrayList<Actor>();
                ArrayList<Actor> top10Path = new ArrayList<Actor>();

                while(top10Path.size() < 10){
                    Actor actor = separationQueue.remove();
                    if (actor.degreeofseperation > 0) top10Path.add(actor);
                }

                for (int i = 0; i < 10; i++){
                    top10Costar.add(costarsQueue.remove());
                }

                System.out.println("How should the top 10 list be displayed?\n" +
                        "By 'lowest degree' of separation or by 'costar' amount.\n>");

                String type = in.nextLine();

                if (type.equals("lowest degree")){
                    System.out.println("Top 10 Worlds with Lowest Degrees of Separation:\n");
                    for (Actor world: top10Path) System.out.println(world);
                }
                else if (type.equals("costar")){
                    System.out.println("Top 10 Worlds with Highest Number of Co-Stars:\n");
                    for (Actor world: top10Costar) System.out.println(world);
                }
                else System.out.println("Invalid choice.");

            }
            else if (choice.equals("quit")){
                gameplay = false;
            }
            else {
                System.out.println("Invalid option. Exiting game...");
                gameplay = false;
            }

        }
    }






}
