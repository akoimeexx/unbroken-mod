package com.akoimeexx.unbroken;

import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class UnbrokenToolPredicates {
    public static final Predicate<PlayerEntity> CREATIVE_PLAYER;
    public static final Predicate<ItemStack> IS_BREAKABLE;
    public static final Predicate<ItemStack> WILL_BREAK;

    public static final boolean test(PlayerEntity player, ItemStack item) {
        return 
            CREATIVE_PLAYER.negate().test(player) && 
            IS_BREAKABLE.and(WILL_BREAK).test(item);
    }

    static {
        CREATIVE_PLAYER = player -> player.isCreative() || player.isSpectator();
        IS_BREAKABLE = item -> item.isDamageable();
        WILL_BREAK = item -> item.getMaxDamage() - item.getDamage() <= 1;
    }
}
