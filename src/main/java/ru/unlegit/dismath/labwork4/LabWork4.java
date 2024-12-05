package ru.unlegit.dismath.labwork4;

public final class LabWork4 {

    private static final int[][] DOT_SEQUENCES = new int[][]{
            new int[]{7, 6, 5, 3, 4},
            new int[]{1, 7, 6, 2, 5, 6},
            new int[]{7, 1, 2, 3, 6, 7},
            new int[]{5, 6, 3, 5, 3, 4},
            new int[]{1, 2, 6, 7, 2, 1}
    };

    public static void main(String[] args) {
        Graph graphG1 = Graph.withEdges(7,
                new Graph.Edge(1, 2), new Graph.Edge(1, 6), new Graph.Edge(1, 7),
                new Graph.Edge(2, 6), new Graph.Edge(2, 7),
                new Graph.Edge(3, 4), new Graph.Edge(3, 5),
                new Graph.Edge(4, 5), new Graph.Edge(4, 6),
                new Graph.Edge(5, 6)
        );
        Graph graphG2 = Graph.fromIncidenceMatrix(new int[][]{
                {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
        });
    }
}