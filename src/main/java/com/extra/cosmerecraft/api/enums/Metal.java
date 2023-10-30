package com.extra.cosmerecraft.api.enums;

public enum Metal {

    IRON(true, false, 2),
    STEEL(false, true, 2),
    TIN(false, false, 1),
    PEWTER(false, true, 5),
    ZINC(false, false, 4),
    BRASS(false, true, 4),
    COPPER(true, false, 1),
    BRONZE(-1, 3, false, true, 3),
    ALUMINUM(-1, 3, false, false, 0),
    DURALUMIN(false, true, 0),
    CHROMIUM(false, false, 0),
    NICROSIL(-1, 1,false,true, 0),
    GOLD(true, false, 8),
    ELECTRUM(false, true, 8),
    CADMIUM(false, false, 20),
    BENDALLOY(false, true, 80),
    ATIUM(false, false, 20);

    private int minTap;
    private int maxTap;
    private boolean isVanilla;
    private boolean isAlloy;
    private int alloDrain;

    Metal(boolean isVanilla, boolean isAlloy, int alloDrain){
        this(-2,6,isVanilla, isAlloy, alloDrain);
    }

    Metal(int minTap, int maxTap, boolean isVanilla, boolean isAlloy, int alloDrain){
        this.maxTap = maxTap;
        this.minTap = minTap;
        this.isVanilla = isVanilla;
        this.isAlloy = isAlloy;
        this.alloDrain = alloDrain;
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

    public int getAllomanticDrain() { return this.alloDrain;}

    public boolean isVanilla(){return this.isVanilla;}

    public boolean isAlloy(){return this.isAlloy;}
}
