package ru.unlegit.dismath.relationship;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Relationship(int[] baseSet, List<OrderedPair> elements) {

    public static Relationship generate(int[] baseSet, GeneratingProcedure generatingProcedure) {
        return new Relationship(baseSet, Arrays.stream(baseSet)
                .boxed()
                .flatMap(left -> Arrays.stream(baseSet)
                        .filter(right -> generatingProcedure.process(left, right))
                        .mapToObj(right -> new OrderedPair(left, right)))
                .toList()
        );
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(OrderedPair::toString)
                .collect(Collectors.joining(",", "{", "}"));
    }

    public Relationship or(Relationship relationship) {
        return new Relationship(baseSet, Stream.concat(elements.stream(), relationship.elements.stream())
                .distinct()
                .sorted()
                .toList()
        );
    }

    public Relationship and(Relationship relationship) {
        return new Relationship(baseSet, elements.stream()
                .filter(relationship.elements::contains)
                .sorted()
                .toList()
        );
    }

    private Stream<OrderedPair> subtract0(Relationship relationship) {
        return elements.stream().filter(element -> !relationship.elements.contains(element));
    }

    public Relationship subtract(Relationship relationship) {
        return new Relationship(baseSet, subtract0(relationship).sorted().toList());
    }

    public Relationship subtractSymmetrically(Relationship relationship) {
        return new Relationship(baseSet, Stream.concat(
                subtract0(relationship),
                relationship.subtract0(this)
        ).sorted().toList());
    }

    public Relationship addition(Relationship universe) {
        return universe.subtract(this);
    }

    public Relationship inversion() {
        return new Relationship(baseSet, elements.stream()
                .filter(element -> elements.contains(element.reverse()))
                .sorted()
                .toList()
        );
    }

    public Relationship composition(Relationship relationship) {
        return new Relationship(baseSet, Stream.concat(elements.stream(), relationship.elements.stream())
                .filter(element -> Arrays.stream(baseSet).anyMatch(parameter ->
                        elements.contains(element.withRight(parameter)) &&
                                relationship.elements.contains(element.withLeft(parameter))
                )).distinct().sorted().toList()
        );
    }

    public Relationship power(int power) {
        if (power < 0) throw new IllegalArgumentException("invalid relationship power " + power);
        if (power == 0) return generate(baseSet, GeneratingProcedure.IDENTITY);

        Relationship result = this;

        for (int i = 1; i < power; i++) {
            result = result.composition(this);
        }

        return result;
    }

    public void printMatrix() {
        int baseSize = baseSet.length;
        int[][] cells = new int[baseSize][baseSize];

        elements.forEach(element -> cells[element.left() - 1][element.right() - 1] = 1);

        for (int i = 0; i < baseSize; i++) {
            System.out.println(Arrays.stream(cells[i])
                    .boxed()
                    .map(Object::toString)
                    .collect(Collectors.joining(" ", "(", ")"))
            );
        }
    }
}