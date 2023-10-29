package com.extra.cosmerecraft.api.enums;

public enum Power {

    ALLOMANCY(1,0.5f,0),
    FERUCHEMY(1,1,0),
    SURGEBINDING(0,0.5f,1),
    AONDOR(1,1,1),
    AWAKENING(0,1,0);

    private float red;
    private float green;
    private float blue;

    Power(float pRed, float pGreen, float pBlue){
        this.red = pRed;
        this.blue = pBlue;
        this.green = pGreen;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getIndex() {
        return ordinal();
    }


    public static Power getPower(int index) {
        for (Power power : values()) {
            if (power.getIndex() == index) {
                return power;
            }
        }
        throw new IllegalArgumentException("Bad Power Index");
    }

    public float getRed(){
        return this.red;
    }

    public float getGreen(){
        return this.green;
    }

    public float getBlue(){
        return this.blue;
    }
}
