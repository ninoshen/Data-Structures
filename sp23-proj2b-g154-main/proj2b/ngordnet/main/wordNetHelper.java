package ngordnet.main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class wordNetHelper {
    private final GraphHelper graph;
    private final HashMap<Integer, ArrayList<String>> toWords; // what node goes with the index
    private final HashMap<String, ArrayList<Integer>> toId; // what nodes contain the word

    public wordNetHelper(String synsetFile, String hyponymFile) {
        graph = new GraphHelper();
        toId = new HashMap<>();
        toWords = new HashMap<>();
        In synsetRead = new In(synsetFile); // file
        In hyponymRead = new In(hyponymFile); // file

        while (synsetRead.hasNextLine()) {
            String[] line = synsetRead.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            ArrayList<String> words = new ArrayList<String>();
            String[] wordLine = line[1].split(" ");

            for (String w : wordLine) {
                words.add(w);
                if (toId.containsKey(w)) {
                    toId.get(w).add(id);
                } else {
                    ArrayList<Integer> ids = new ArrayList<Integer>();
                    ids.add(id);
                    toId.put(w, ids);
                }
            }
            toWords.put(id, words);
        }

        while (hyponymRead.hasNextLine()) {
            String[] line = hyponymRead.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            for (int i = 0; i < line.length; i++) {
                graph.addEdge(id, Integer.parseInt(line[i]));
            }
        }
    }

    public Set<String> getHyponym(String word) {
        Set<String> returned = new HashSet<>();
        if (!(toId.containsKey(word))) {
            return null;
        } else {
            Set<Integer> synsetIds = new HashSet<>();
            for (int i : toId.get(word)) {
                for (int g : GraphHelper.bfs(graph, i)) {
                    synsetIds.add(g);
                }
            }
            for (int i : synsetIds) {
                for (String w : toWords.get(i)) {
                    returned.add(w);
                }
            }
            return returned;
        }
    }
}

