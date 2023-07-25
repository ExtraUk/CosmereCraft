package com.extra.cosmerecraft.api.enums;

public enum AllMetal {
    IRON,
    STEEL,
    TIN,
    PEWTER,
    ZINC,
    BRASS,
    COPPER,
    BRONZE,
    ALUMINUM,
    DURALUMIN,
    CHROMIUM,
    NICROSIL,
    GOLD,
    ELECTRUM,
    CADMIUM,
    BENDALLOY,
    BAUXITE,
    NICKEL,
    SILVER,
    BISMUTH,
    ATIUM;

    public String getName() {
        return name().toLowerCase();
    }

    public int getIndex() {
        return ordinal();
    }


    public static AllMetal getMetal(int index) {
        for (AllMetal metal : values()) {
            if (metal.getIndex() == index) {
                return metal;
            }
        }
        throw new IllegalArgumentException("Bad Metal Index");
    }
}
