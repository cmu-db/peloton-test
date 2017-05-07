package edu.cmu.cs.db.peloton.test.generate.defn.operators;

import java.util.*;

/**
 * Created by tianyuli on 5/1/17.
 */
public enum ConstantOption {
    NO_CONSTANT, NOT_ALL_CONSTANTS, NO_RESTRICTION;

    private static Map<ConstantOption, Set<ConstantOption>> gt;
    static {
        gt = new EnumMap<>(ConstantOption.class);
        gt.put(NO_CONSTANT, EnumSet.of(NOT_ALL_CONSTANTS, NO_RESTRICTION));
        gt.put(NOT_ALL_CONSTANTS, EnumSet.of(NO_RESTRICTION));
        gt.put(NO_RESTRICTION, EnumSet.noneOf(ConstantOption.class));
    }

    public static ConstantOption upperBound(ConstantOption... constantOptions) {
        return Collections.max(Arrays.asList(constantOptions), (x, y) -> {
            if (x == y) {
                return 0;
            }
            return gt.get(x).contains(y) ? 1 : -1;
        });
    }
}
