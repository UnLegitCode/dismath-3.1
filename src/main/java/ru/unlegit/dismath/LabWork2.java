package ru.unlegit.dismath;

import ru.unlegit.dismath.relationship.GeneratingProcedure;
import ru.unlegit.dismath.relationship.MatrixRelationship;

public final class LabWork2 {

    private static MatrixRelationship algorithm1(MatrixRelationship A) {
        MatrixRelationship C = A;
        MatrixRelationship S = A.composition(A);

        int compositionCount = 1;

        while(!(S.inclusion(C))) {
            C = C.or(S);
            S = S.composition(A);

            compositionCount++;
        }

        System.out.println("Composition count: " + compositionCount);

        return C;
    }

    private static MatrixRelationship algorithm2(MatrixRelationship A) {
        MatrixRelationship C = A;
        MatrixRelationship C2 = C.composition(C);

        int compositionCount = 1;

        while(!(C2.inclusion(C))) {
            C = C.or(C2);
            C2 = C.composition(C);

            compositionCount++;
        }

        System.out.println("Composition count: " + compositionCount);

        return C;
    }

    private static MatrixRelationship algorithm3(MatrixRelationship A) {
        MatrixRelationship C = A;

        int containingCheckAmount = 0;

        for (int z = 0; z < A.size(); z++) {
            for (int x = 0; x < A.size(); x++) {
                containingCheckAmount++;

                if (!C.contains(x, z)) continue;

                for (int y = 0; y < A.size(); y++) {
                    containingCheckAmount++;

                    if (C.contains(z, y)) {
                        C = C.with(x, y, true);
                    }
                }
            }
        }

        System.out.println("Containing check amount: " + containingCheckAmount);

        return C;
    }

    public static void main(String[] args) {
        MatrixRelationship relationship = MatrixRelationship.generate(
                10, (left, right) -> (left == right) || (right == (left + 1))
        );
        MatrixRelationship transitiveClosure = algorithm3(relationship);
    }
}