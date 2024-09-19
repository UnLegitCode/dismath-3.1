package ru.unlegit.dismath.relationship;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.EnumSet;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum DerivedRelationshipProperty {

    NOT_STRICT_LINEAR_ORDER("нестрогий линейный порядок", EnumSet.of(
            RelationshipProperty.REFLEXIVITY, RelationshipProperty.ANTI_SYMMETRY,
            RelationshipProperty.TRANSITIVITY, RelationshipProperty.COMPLETENESS
    )),
    STRICT_LINEAR_ORDER("строгий линейный порядок", EnumSet.of(
            RelationshipProperty.ANTI_REFLEXIVITY, RelationshipProperty.ANTI_SYMMETRY,
            RelationshipProperty.TRANSITIVITY, RelationshipProperty.COMPLETENESS
    )),
    NOT_STRICT_ORDER("нестрогий порядок", EnumSet.of(
            RelationshipProperty.REFLEXIVITY, RelationshipProperty.ANTI_SYMMETRY, RelationshipProperty.TRANSITIVITY
    )),
    STRICT_ORDER("строгий порядок", EnumSet.of(
            RelationshipProperty.ANTI_REFLEXIVITY, RelationshipProperty.ANTI_SYMMETRY, RelationshipProperty.TRANSITIVITY
    )),
    LINEAR_ORDER("прямой порядок", EnumSet.of(
            RelationshipProperty.ANTI_SYMMETRY, RelationshipProperty.TRANSITIVITY, RelationshipProperty.COMPLETENESS
    )),
    EQUIVALENCE("эквивалентность", EnumSet.of(
            RelationshipProperty.REFLEXIVITY, RelationshipProperty.SYMMETRY, RelationshipProperty.TRANSITIVITY
    )),
    ORDER("порядок", EnumSet.of(RelationshipProperty.ANTI_SYMMETRY, RelationshipProperty.TRANSITIVITY)),
    TOLERANCE("толерантность", EnumSet.of(RelationshipProperty.REFLEXIVITY, RelationshipProperty.SYMMETRY));

    public static DerivedRelationshipProperty findDerivedProperty(Set<RelationshipProperty> properties) {
        for (DerivedRelationshipProperty property : values()) {
            if (properties.containsAll(property.basicProperties)) return property;
        }

        return null;
    }

    @Getter
    String displayName;
    Set<RelationshipProperty> basicProperties;
}