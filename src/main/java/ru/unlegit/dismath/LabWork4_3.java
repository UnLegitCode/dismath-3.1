package ru.unlegit.dismath;

import ru.unlegit.dismath.labwork4.Graph;
import ru.unlegit.dismath.util.JavaUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LabWork4_3 {

    private static Graph.Edge[][] findKElementSets(Graph graph, int k) {
        return findKElementSets(graph.kruskalMST(), k).stream()
                .map(list -> list.toArray(Graph.Edge[]::new))
                .toArray(Graph.Edge[][]::new);
    }

    private static List<List<Graph.Edge>> findKElementSets(List<Graph.Edge> mst, int k) {
        List<List<Graph.Edge>> result = new ArrayList<>();
        List<Graph.Edge> edges = new ArrayList<>(mst);

        combinations(edges, k, 0, new ArrayList<>(), result);

        result.removeIf(set -> !isBridgeSet(mst, set));

        return result;
    }

    private static void combinations(
            List<Graph.Edge> edges, int k, int start, List<Graph.Edge> current, List<List<Graph.Edge>> result
    ) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < edges.size(); i++) {
            current.add(edges.get(i));
            combinations(edges, k, i + 1, current, result);
            current.removeLast();
        }
    }

    private static boolean isBridgeSet(List<Graph.Edge> mst, List<Graph.Edge> set) {
        List<Graph.Edge> modifiedMST = new ArrayList<>(mst);

        modifiedMST.removeAll(set);

        return !isConnected(modifiedMST, mst.getFirst().start() - 1, mst.size() + 1);
    }

    private static boolean isConnected(List<Graph.Edge> edges, int startVertex, int vertexCount) {
        boolean[] visited = new boolean[vertexCount];

        dfs(edges, startVertex, visited);

        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i]) return false;
        }

        return true;
    }

    private static void dfs(List<Graph.Edge> edges, int vertex, boolean[] visited) {
        visited[vertex] = true;

        for (Graph.Edge edge : edges) {
            if (edge.start() - 1 == vertex && !visited[edge.end() - 1]) {
                dfs(edges, edge.end() - 1, visited);
            } else if (edge.end() - 1 == vertex && !visited[edge.start() - 1]) {
                dfs(edges, edge.start() - 1, visited);
            }
        }
    }

    public static void main(String[] args) {
        Graph graph = Graph.withEdges(
                4, new Graph.Edge(1, 2), new Graph.Edge(2, 3),
                new Graph.Edge(3, 4), new Graph.Edge(4, 1), new Graph.Edge(1, 3)
        );

        System.out.println(JavaUtil.formatMatrix(graph.adjacencyMatrix()));

        int k = 2;
        Graph.Edge[][] result = findKElementSets(graph, k);

        for (Graph.Edge[] edges : result) {
            System.out.println(Arrays.toString(edges));
        }
    }
}