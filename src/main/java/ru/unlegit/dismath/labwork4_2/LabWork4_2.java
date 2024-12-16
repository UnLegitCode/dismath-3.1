package ru.unlegit.dismath.labwork4_2;

import lombok.Cleanup;
import ru.unlegit.dismath.labwork4.Graph;
import ru.unlegit.dismath.util.JavaUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class LabWork4_2 {

    private static final long TIME_LIMIT = TimeUnit.SECONDS.toNanos(10L);

    private static Graph randomGraph(int dotAmount, int graphAmount) {
        int[][] result = new int[dotAmount][dotAmount];
        int currentEdges = 0;

        while (currentEdges < graphAmount) {
            int row = ThreadLocalRandom.current().nextInt(dotAmount - 1);
            int column = row + 1 + ThreadLocalRandom.current().nextInt(dotAmount - row - 1);

            if (result[row][column] == 0) {
                result[row][column] = 1;
                currentEdges++;
            }
        }

        return Graph.fromAdjacencyMatrix(result);
    }

    public static void main(String[] args) {
        List<Graph> graphs = new CopyOnWriteArrayList<>();
        int threads = Runtime.getRuntime().availableProcessors();
        @Cleanup
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int n = 8; n <= 10; n++) {
            for (int h = 0; h < 7; h++) {
                int finalN = n; //thread-safety moment
                int m = n + h;
                List<Future<?>> futures = new LinkedList<>();
                long start = System.nanoTime();

                JavaUtil.repeat(threads, () -> futures.add(executor.submit(() -> {
                    while (!Thread.interrupted()) {
                        graphs.add(randomGraph(finalN, m));

                        if ((System.nanoTime() - start) >= TIME_LIMIT) break;
                    }
                })));

                while (!futures.stream().allMatch(Future::isDone)) ; //awaiting all threads completion

                int totalGraphs = graphs.size();
                int eulerianGraphs = (int) graphs.stream().filter(Graph::isEulerian).count();
                int hamiltonian = (int) graphs.stream().filter(Graph::isHamiltonian).count();

                System.out.printf(
                        "n = %d; h = %d: total graphs: %d; eulerianGraphs = %d; hamiltonian graphs: %d%n",
                        n, h, totalGraphs, eulerianGraphs, hamiltonian
                );
            }

            System.out.println();
        }
    }
}