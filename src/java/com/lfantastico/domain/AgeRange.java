package com.lfantastico.domain;

public class AgeRange {
    private int minAge;
    private int maxAge;

    /**
     * @Deprecated to produce a warning if this constructor is called directly.
     */
    @Deprecated
    public AgeRange() {
    }

    public AgeRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Invalid range: "
                    + min + ">" + max);
        }
        minAge = min;
        maxAge = max;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public boolean contains(int age) {
        return age >= minAge && age <= maxAge;
    }

    public boolean intersects(AgeRange range) {
        return range.getMaxAge() >= minAge && range.getMinAge() <= maxAge;
    }

    @Override
    public String toString() {
        return minAge + "-" + maxAge;
    }
}
