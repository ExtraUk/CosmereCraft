package com.extra.cosmerecraft.loot;

import com.extra.cosmerecraft.api.enums.Metal;
import com.extra.cosmerecraft.feruchemy.data.FeruchemistCapability;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


public class ChromiumFortuneModifier extends LootModifier {

    protected ChromiumFortuneModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    public static final Supplier<Codec<ChromiumFortuneModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, ChromiumFortuneModifier::new)));

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        AtomicReference<ObjectArrayList<ItemStack>> ret = new AtomicReference<>(new ObjectArrayList<>());

        if(tool != null && blockState != null && entity instanceof Player && !tool.getOrCreateTag().getBoolean("flag")){
            entity.getCapability(FeruchemistCapability.PLAYER_CAP_FERUCHEMY).ifPresent(data -> {
                int tappingLevel = data.tappingLevel(Metal.CHROMIUM);
                if(tappingLevel != 0) {
                    ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();
                    fakeTool.getTag().putBoolean("flag", true);
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
                    enchantments.put(Enchantments.BLOCK_FORTUNE, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, fakeTool) + tappingLevel);

                    EnchantmentHelper.setEnchantments(enchantments, fakeTool);

                    LootContext.Builder builder = new LootContext.Builder(context);
                    builder.withParameter(LootContextParams.TOOL, fakeTool);

                    LootContext newContext = builder.create(LootContextParamSets.BLOCK);
                    LootTable lootTable = context.getLevel().getServer().getLootTables().get(blockState.getBlock().getLootTable());

                    ret.set(lootTable.getRandomItems(newContext));
                }
                else{
                    ret.set(generatedLoot);
                }
            });
            return ret.get();
        }
        return generatedLoot;
    }
}
