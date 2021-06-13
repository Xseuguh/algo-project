package Graph;

import java.util.*;

public class Combination {
    private Map<Vertex, Set<Edge>> adjacencyList;
    private List<int[]> combinations;
    private Map<Integer,Vertex> verticesIDMap;

    public Map<Integer,Vertex> getVerticesIDMap() {
        return this.verticesIDMap;
    }

    public Combination(Map<Vertex, Set<Edge>> adjacencyList) {
        this.adjacencyList = adjacencyList;
        this.verticesIDMap = graphVertexIDs();
    }

    public List<List<Vertex>> getNodePairs() {
        this.combinations = generate(adjacencyList.size(), 2);
        List<List<Vertex>> verticesCombinationsTemp = new ArrayList<List<Vertex>>();
        for (int[] intList : combinations) {
            List<Vertex> vertexPair = new ArrayList<Vertex>();
            vertexPair.add(verticesIDMap.get(intList[0]));
            vertexPair.add(verticesIDMap.get(intList[1]));
            if (!vertexPair.get(0).equals(vertexPair.get(1))) {
                verticesCombinationsTemp.add(vertexPair);
            }
        }
        return verticesCombinationsTemp;
    }

    public void helper(List<int[]> combinationsT, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinationsT.add(combination);
        } else {
            int max = Math.min(end, end + 1 - data.length + index);
            for (int i = start; i <= max; i++) {
                data[index] = i;
                helper(combinationsT, data, i + 1, end, index + 1);
            }
        }
    }

    public List<int[]> generate(int n, int r) {
        List<int[]> combinationsTemp = new ArrayList<>();
        helper(combinationsTemp, new int[r], 0, n - 1, 0);
        return combinationsTemp;
    }
    
    public Map<Integer,Vertex> graphVertexIDs() {
        Map<Integer,Vertex> res = new HashMap<Integer,Vertex>();
        int i = 0;
        for (Vertex v : adjacencyList.keySet()) {
            res.put(i,v);
            i++;
        }
        return res;
    }
}
