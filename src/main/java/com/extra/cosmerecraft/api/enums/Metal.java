package com.extra.cosmerecraft.api.enums;

public enum Metal {

    IRON(true, false),
    STEEL(false, true),
    TIN(-1, 1, false, false),
    PEWTER(false, true),
    ZINC(false, false),
    BRASS(false, true),
    COPPER(true, false),
    BRONZE(-1, 3, false, true),
    ALUMINUM(-1, 3, false, false),
    DURALUMIN(false, true),
    CHROMIUM(false, false),
    NICROSIL(-1, 1,false,true),
    GOLD(true, false),
    ELECTRUM(false, true),
    CADMIUM(false, false),
    BENDALLOY(false, true),
    ATIUM(false, false);

    private int minTap;
    private int maxTap;
    private boolean isVanilla;
    private boolean isAlloy;

    Metal(boolean isVanilla, boolean isAlloy){
        this(-2,3,isVanilla, isAlloy);
    }

    Metal(int minTap, int maxTap, boolean isVanilla, boolean isAlloy){
        this.maxTap = maxTap;
        this.minTap = minTap;
        this.isVanilla = isVanilla;
        this.isAlloy = isAlloy;
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

    public int getMaxTap(){
        return this.maxTap;
    }

    public boolean isVanilla(){return this.isVanilla;}

    public boolean isAlloy(){return this.isAlloy;}
}
