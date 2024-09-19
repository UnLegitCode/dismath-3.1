package ru.unlegit.dismath.relationship;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record OrderedPairListRelationship(
        int[] baseSet, List<OrderedPair> elements
) implements Relationship<OrderedPairListRelationship> {

    private static Stream<OrderedPair> generatePairs(int[] baseSet, GeneratingProcedure generatingProcedure) {
        return Arrays.stream(baseSet)
                .boxed()
                .flatMap(left -> Arrays.stream(baseSet)
                        .filter(right -> generatingProcedure.process(left, right))
                        .mapToObj(right -> new OrderedPair(left, right)));
    }

    public static OrderedPairListRelationship generate(int[] baseSet, GeneratingProcedure generatingProcedure) {
        return new OrderedPairListRelationship(baseSet, generatePairs(baseSet, generatingProcedure).toList());
    }

    @Override
    public String toString() {
        return elements.stream()
                .map(OrderedPair::toString)
                .collect(Collectors.joining(",", "{", "}"));
    }

    public boolean inclusion(OrderedPairListRelationship relationship) {
        return new HashSet<>(relationship.elements).containsAll(elements);
    }

    public boolean equals(OrderedPairListRelationship relationship) {
        return elements.equals(relationship.elements);
    }

    public boolean strictInclusion(OrderedPairListRelationship relationship) {
        return inclusion(relationship) && !equals(relationship);
    }

    public OrderedPairListRelationship or(OrderedPairListRelationship relationship) {
        return new OrderedPairListRelationship(baseSet, Stream.concat(elements.stream(), relationship.elements.stream())
                .distinct()
                .sorted()
                .toList()
        );
    }

    public OrderedPairListRelationship and(OrderedPairListRelationship relationship) {
        return new OrderedPairListRelationship(baseSet, elements.stream()
                .filter(relationship.elements::contains)
                .sorted()
                .toList()
        );
    }

    private Stream<OrderedPair> subtract0(OrderedPairListRelationship relationship) {
        return elements.stream().filter(element -> !relationship.elements.contains(element));
    }

    public OrderedPairListRelationship subtract(OrderedPairListRelationship relationship) {
        return new OrderedPairListRelationship(baseSet, subtract0(relationship).sorted().toList());
    }

    public OrderedPairListRelationship subtractSymmetrically(OrderedPairListRelationship relationship) {
        return new OrderedPairListRelationship(baseSet, Stream.concat(
                subtract0(relationship),
                relationship.subtract0(this)
        ).sorted().toList());
    }

    @Override
    public OrderedPairListRelationship addition() {
        return generate(baseSet, GeneratingProcedure.UNIVERSE).subtract(this);
    }

    public OrderedPairListRelationship inversion() {
        return new OrderedPairListRelationship(baseSet, elements.stream()
                .filter(element -> elements.contains(element.reverse()))
                .sorted()
                .toList()
        );
    }

    public OrderedPairListRelationship composition(OrderedPairListRelationship relationship) {
        return new OrderedPairListRelationship(baseSet, generatePairs(baseSet, GeneratingProcedure.UNIVERSE)
                .filter(element -> Arrays.stream(baseSet).anyMatch(parameter ->
                        elements.contains(element.withRight(parameter)) &&
                        relationship.elements.contains(element.withLeft(parameter))
                )).distinct().sorted().toList()
        );
    }

    public OrderedPairListRelationship power(int power) {
        if (power < 0) throw new IllegalArgumentException("invalid relationship power " + power);
        if (power == 0) return generate(baseSet, GeneratingProcedure.IDENTITY);

        OrderedPairListRelationship result = this;

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