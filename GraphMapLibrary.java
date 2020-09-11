//import net.datastructures.Node;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/* Author: Maria Roodnitsky and Chris Wright
   Date: 11/5/2019
   Function: Library of GraphMap functions: bfs, average path, missingVertices, and getPath
 */

public class GraphMapLibrary {
    static double pathSum;

    public GraphMapLibrary() {
    }

    /* Creates a set of unreachable vertices */

    public static <V, E> Set<V> missingVertices(AdjacencyMapGraph<V, E> graph, AdjacencyMapGraph<V, E> subgraph) {
      //initialize a set to contain all of the vertices that are not in the subgraph but exist in the data universe
       Set<V> missingSet = new HashSet<>();

       //for each vertex in the universe, check to see if it exists in the subgraph; if it doesn't, add it to the set
       for (V vertex : graph.vertices()) {
           if (graph.hasVertex(vertex) && !subgraph.hasVertex(vertex)) {
               missingSet.add(vertex);
           }
       }
       return missingSet;
   }

    /* Finds the average path length of a tree which has undergone bfs*/

    public static <V, E> double averageSeparation(AdjacencyMapGraph<V, E> tree, V root){
        //reset pathSum
        pathSum = 0;
        //find the number of vertices that can be reached in this tree
        double pathNodes = tree.numVertices() - 1;

        if (pathNodes < 0) return -1;
        else if (pathNodes == 0) return 0;
        else {
            pathSumCalculator(tree, root, 0);
            return pathSum / pathNodes;
        }
   }

    /* Helper function for finding the average path length*/

    public static <V, E> void pathSumCalculator(AdjacencyMapGraph<V, E> tree, V root, int depth){
        //add the length of the path to this node to the sum
        pathSum += depth;

        //end if this is a leaf vertex
        if (tree.inDegree(root) == 0)
            return;

        //increment depth and add the path lengths of this vertex's children
        for (V child: tree.inNeighbors(root)){
            pathSumCalculator(tree, child, depth + 1);
        }
    }

    /* breadth first search */

    public static <V, E> AdjacencyMapGraph<V, E> bfs(AdjacencyMapGraph<V, E> g, V source){

        //initialize backTrack
        AdjacencyMapGraph<V, E> backTrack = new AdjacencyMapGraph<V, E>();

        //return a null tree if the source is not in the world
        if(!g.hasVertex(source)){
            return backTrack;
        }

        //load start vertex with null parent
        backTrack.insertVertex(source);

        //Set to track which vertices have already been visited
        Set<V> visited = new HashSet<V>();

        //queue to implement BFS
        Queue<V> queue = new LinkedList<V>();

        //enqueue source vertex
        queue.add(source);

        //add source to visited Set
        visited.add(source);

        //loop until no more vertices
        while (!queue.isEmpty()) {
            V actor = queue.remove(); //dequeue
                for(V costar: g.outNeighbors(actor)){
                    if (!visited.contains(costar)) { //if costar not visited, then costar is discovered from this vertex
                        visited.add(costar); //add costar to visited Set
                        queue.add(costar); //enqueue costar
                        backTrack.insertVertex(costar);
                        backTrack.insertDirected(costar, actor, g.getLabel(costar, actor));
                    }
                }
            }
            return backTrack;
    }

    public static <V,E> ArrayList<V> getPath(AdjacencyMapGraph<V, E> tree, V goal, V source) {
        //check that DFS or BFS have already been run from start
        AdjacencyMapGraph<V, E> backTrack = bfs(tree, goal);

        //return an empty path if the source cannot trace back to the center of the universe (goal)
        if (!backTrack.hasVertex(source)) {
            return new ArrayList<V>();
        }
        //start from source vertex and work backward to goal vertex
        ArrayList<V> path = new ArrayList<V>(); //this will hold the path source to goal

        V current = source; //start at source

        //loop from end vertex back to start vertex
        do {
            path.add(current); //add this vertex to the path
            for (V neighbor: backTrack.outNeighbors(current)){
                current = neighbor;
            }
        } while (!current.equals(goal));
        path.add(current); //add the end to path
        return path;
    }
}

