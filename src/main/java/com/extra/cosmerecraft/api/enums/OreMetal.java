package com.extra.cosmerecraft.api.enums;

public enum OreMetal {
    TIN,
    ZINC,
    CADMIUM,
    BAUXITE,
    NICKEL,
    SILVER,
    BISMUTH,
    CHROMIUM;

    public String getName() {
        return name().toLowerCase();
    }

    public int getIndex() {
        return ordinal();
    }


    public static OreMetal getMetal(int index) {
        for (OreMetal metal : values()) {
            if (metal.getIndex() == index) {
                return metal;
            }
        }
        throw new IllegalArgumentException("Bad Metal Index");
    }
}
