package ngordnet.main;

import edu.princeton.cs.algs4.Queue;

import java.util.*;

public class GraphHelper {
    HashMap<Integer, LinkedList<Integer>> adjLst = new HashMap<>();

    public GraphHelper() {
    }

    public void addEdge(int v, int w) {
        if (v != w) {
            if (!(adjLst.containsKey(v))) {
                adjLst.put(v, new LinkedList<>());
            }
            adjLst.get(v).add(w);
        }
    }

    public static Set<Integer> bfs(GraphHelper G, int id) {
        Set<Integer> marked = new HashSet<>();
        Queue<Integer> fringe = new Queue<>();
        fringe.enqueue(id); // start up vertex
        marked.add(id);
        while (!fringe.isEmpty()) {
            int v = fringe.dequeue();
            if (G.adjLst.get(v) != null) {
                for (Integer i : G.adjLst.get(v)) {
                    if (!(marked.contains(i))) {
                        fringe.enqueue(i);
                        marked.add(i);
                    }
                }
            }
        }
        return marked;
    }
}
