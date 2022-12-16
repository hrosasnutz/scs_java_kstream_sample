package io.spring.scs_java_kstream_sample.dto.customer;

import java.util.Random;

public enum Type {
    INTERNAL, EXTERNAL, LOYAL, POTENTIAL, INTERMEDIATE, FORMER, UNKNOWN;
    
    public static Type random() {
        var random = new Random().nextInt(6);
        return switch (random) {
            case 0 -> INTERNAL;
            case 1 -> EXTERNAL;
            case 2 -> LOYAL;
            case 3 -> POTENTIAL;
            case 4 -> INTERMEDIATE;
            case 5 -> FORMER;
            default -> UNKNOWN;
        };
    }
}
