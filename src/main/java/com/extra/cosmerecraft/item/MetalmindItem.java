package com.extra.cosmerecraft.item;

import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MetalmindItem extends Item implements ICurioItem {

    private int charge = 0;
    private int key = 0;

    public MetalmindItem(Properties properties) {
        super(properties.stacksTo(1));
    }
}
