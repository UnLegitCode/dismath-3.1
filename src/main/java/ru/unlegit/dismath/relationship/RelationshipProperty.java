package ru.unlegit.dismath.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.unlegit.dismath.util.MathUtil;

import java.util.EnumSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum RelationshipProperty {

    REFLEXIVITY("рефлексивность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            for (int i = 0; i < relationship.size(); i++) {
                if (relationship.matrix()[i][i] == 0) return PropertyCheckResult.fail(
                        "Отношение не рефлексивно потому, что ему не принадлежит пара (%d,%d)".formatted(++i, i)
                );
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    ANTI_REFLEXIVITY("антирефлексивность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            for (int i = 0; i < relationship.size(); i++) {
                if (relationship.matrix()[i][i] == 1) return PropertyCheckResult.fail(
                        "Отношение не антирефлексивно потому, что ему принадлежит пара (%d,%d)".formatted(++i, i)
                );
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    SYMMETRY("симметричность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            int iLimit  = MathUtil.getHalf(relationship.size());

            for (int i = 0; i < iLimit; i++) {
                for (int j = iLimit; j < relationship.size(); j++) {
                    if (relationship.matrix()[i][j] > relationship.matrix()[j][i]) return PropertyCheckResult.fail(
                            "Отношение не симметрично потому, что пара (%d,%d) принадлежит ему, а пара (%d,%d) — нет"
                                    .formatted(++i, ++j, j, i)
                    );
                }
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    ANTI_SYMMETRY("антисимметричность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            int iLimit  = MathUtil.getHalf(relationship.size());

            for (int i = 0; i < iLimit; i++) {
                for (int j = iLimit; j < relationship.size(); j++) {
                    if ((relationship.matrix()[i][j] & relationship.matrix()[j][i]) == 1) {
                        return PropertyCheckResult.fail(
                                "Отношение не антисимметрично потому, что ему принадлежат пары (%d,%d) и (%d, %d)"
                                        .formatted(++i, ++j, j, i)
                        );
                    }
                }
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    TRANSITIVITY("транзитивность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            for (int i = 0; i < relationship.size(); i++) {
                for (int j = 0; j < relationship.size(); j++) {
                    if (relationship.matrix()[i][j] == 0) continue;

                    for (int k = 0; k < relationship.size(); k++) {
                        if (relationship.matrix()[j][k] == 0) continue;

                        if (relationship.matrix()[i][k] == 0) return PropertyCheckResult.fail(
                                ("Отношение не транзитивно потому, что пары (%d,%d) и (%d,%d) принадлежат ему, " +
                                        "а пара (%d,%d) - нет").formatted(++i, ++j, j, ++k, i, k)
                        );
                    }
                }
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    ANTI_TRANSITIVITY("антитранзитивность") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            for (int i = 0; i < relationship.size(); i++) {
                for (int j = 0; j < relationship.size(); j++) {
                    if (relationship.matrix()[i][j] == 0) continue;

                    for (int k = 0; k < relationship.size(); k++) {
                        if (relationship.matrix()[j][k] == 0) continue;

                        if (relationship.matrix()[i][k] == 1) return PropertyCheckResult.fail(
                                ("Отношение не антитранзитивно потому, что ему принадлежат пары " +
                                        "(%d,%d), (%d,%d) и (%d,%d)").formatted(++i, ++j, j, ++k, i, k)
                        );
                    }
                }
            }

            return PropertyCheckResult.SUCCESS;
        }
    },
    COMPLETENESS("полнота") {
        @Override
        protected PropertyCheckResult check(MatrixRelationship relationship) {
            int iLimit  = MathUtil.getHalf(relationship.size());

            for (int i = 0; i < iLimit; i++) {
                for (int j = iLimit; j < relationship.size(); j++) {
                    if ((relationship.matrix()[i][j] | relationship.matrix()[j][i]) == 0) {
                        return PropertyCheckResult.fail(
                                "Отношение R не полно потому, что в нём нет пары (%d,%d) и нет пары (%d,%d)"
                                        .formatted(++i, ++j, j, i)
                        );
                    }
                }
            }

            return PropertyCheckResult.SUCCESS;
        }
    };

    public static Set<RelationshipProperty> findProperties(
            MatrixRelationship relationship, String relationshipDisplayName
    ) {
        Set<RelationshipProperty> properties = EnumSet.noneOf(RelationshipProperty.class);

        for (RelationshipProperty property : values()) {
            PropertyCheckResult result = property.check(relationship);

            if (result.isSuccess()) {
                properties.add(property);
            } else {
//                System.out.printf("Множество %s: %s%n", relationshipDisplayName, result.getCause())
            }
        }

        return properties;
    }

    private final String displayName;

    protected abstract PropertyCheckResult check(MatrixRelationship relationship);
}