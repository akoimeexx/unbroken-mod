package com.akoimeexx.unbroken.mixin;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.akoimeexx.unbroken.UnbrokenToolPredicates;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class UnbrokenItemStackMixin {
	@Inject(
		at=@At("HEAD"), 
		method="useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
		cancellable=true
	)
	private void preventBreakage(
		ItemUsageContext context, 
		CallbackInfoReturnable<ActionResult> info
	) {
		if (
			UnbrokenToolPredicates.test(context.getPlayer(), context.getStack())
		) {
			info.setReturnValue(ActionResult.FAIL);
		}
	}

	static {
        PlayerBlockBreakEvents.BEFORE.register(
            new PlayerBlockBreakEvents.Before() {
                public boolean beforeBlockBreak(
                    World world, 
                    PlayerEntity player, 
                    BlockPos pos, 
                    BlockState state, 
					BlockEntity blockEntity
                ) {
                    return !UnbrokenToolPredicates.test(player, player.getMainHandStack());
                }
            }
        );
	}
}