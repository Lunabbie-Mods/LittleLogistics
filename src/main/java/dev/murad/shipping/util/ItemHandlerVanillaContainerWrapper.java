package dev.murad.shipping.util;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ItemHandlerVanillaContainerWrapper extends Container {
    ItemStackHandler getRawHandler();

    default int getContainerSize() {
        return getRawHandler().getSlots();
    }

    default boolean isEmpty() {
        for (int i = 0; i <  getRawHandler().getSlots(); i++) {
            if(! getRawHandler().getStackInSlot(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    default ItemStack getItem(int pIndex) {
        return getRawHandler().getStackInSlot(pIndex);
    }

    default ItemStack removeItem(int pIndex, int pCount) {
        return getRawHandler().extractItem(pIndex, pCount, false);
    }

    default ItemStack removeItemNoUpdate(int pIndex) {
        var stack = getRawHandler().getStackInSlot(pIndex);
        getRawHandler().setStackInSlot(pIndex, ItemStack.EMPTY);
        return stack;
    }

    default void setItem(int pIndex, ItemStack pStack) {
         getRawHandler().setStackInSlot(pIndex, pStack);
    }

    default void setChanged() {

    }

    default void clearContent() {
        for (int i = 0; i <  getRawHandler().getSlots(); i++) {
             getRawHandler().setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
