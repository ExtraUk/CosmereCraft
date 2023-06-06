package com.extra.cosmerecraft.api.enums;

public enum Metal {
    IRON,
    STEEL,
    TIN(-1),
    PEWTER,
    ZINC,
    BRASS,
    COPPER,
    BRONZE(-1),
    ALUMINUM(-1),
    DURALUMIN,
    CHROMIUM,
    NICROSIL,
    GOLD,
    ELECTRUM,
    CADMIUM,
    BENDALLOY,
    ATIUM;

    private int minTap;

    Metal() {
        this(-2);
    }

    Metal(int minTap) {
        this.minTap = minTap;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getIndex() {
        return ordinal();
    }


    public static Metal getMetal(int index) {
        for (Metal metal : values()) {
            if (metal.getIndex() == index) {
                return metal;
            }
        }
        throw new IllegalArgumentException("Bad Metal Index");
    }

    public int getMinTap(){
        return this.minTap;
    }
}
