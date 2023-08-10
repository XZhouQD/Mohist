package org.bukkit.craftbukkit.v1_20_R1.inventory;

import com.mohistmc.inventory.MohistSpecialIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.recipe.CookingBookCategory;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.ArrayList;
import java.util.List;

public interface CraftRecipe extends Recipe {

    void addToCraftingManager();

    default Ingredient toNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
        Ingredient stack;

        if (bukkit == null) {
            stack = Ingredient.EMPTY;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = new Ingredient(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.Ingredient.ItemValue(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else if (bukkit instanceof RecipeChoice.ExactChoice) {
            stack = new Ingredient(((RecipeChoice.ExactChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.world.item.crafting.Ingredient.ItemValue(CraftItemStack.asNMSCopy(mat))));
            stack.exact = true;
        }else if (bukkit instanceof MohistSpecialIngredient mohistSpecialIngredient) {
            stack = mohistSpecialIngredient.ingredient();
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        stack.getItems();
        if (stack.isVanilla() && requireNotEmpty && stack.getItems().length == 0) {
            throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
        } else {
            return stack;
        }
    }

    public static RecipeChoice toBukkit(Ingredient list) {
        list.getItems();
        if (!list.isVanilla()) {
            return new MohistSpecialIngredient(list);
        }

        net.minecraft.world.item.ItemStack[] items = list.getItems();
        if (items.length == 0) {
            return null;
        }

        if (list.exact) {
            List<org.bukkit.inventory.ItemStack> choices = new ArrayList<>(items.length);
            for (net.minecraft.world.item.ItemStack i : items) {
                choices.add(CraftItemStack.asBukkitCopy(i));
            }

            return new RecipeChoice.ExactChoice(choices);
        } else {

            List<org.bukkit.Material> choices = new ArrayList<>(items.length);
            for (net.minecraft.world.item.ItemStack i : items) {
                choices.add(CraftMagicNumbers.getMaterial(i.getItem()));
            }

            return new RecipeChoice.MaterialChoice(choices);
        }
    }

    public static net.minecraft.world.item.crafting.CraftingBookCategory getCategory(CraftingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CraftingBookCategory.valueOf(bukkit.name());
    }

    public static CraftingBookCategory getCategory(net.minecraft.world.item.crafting.CraftingBookCategory nms) {
        return CraftingBookCategory.valueOf(nms.name());
    }

    public static net.minecraft.world.item.crafting.CookingBookCategory getCategory(CookingBookCategory bukkit) {
        return net.minecraft.world.item.crafting.CookingBookCategory.valueOf(bukkit.name());
    }

    public static CookingBookCategory getCategory(net.minecraft.world.item.crafting.CookingBookCategory nms) {
        return CookingBookCategory.valueOf(nms.name());
    }
}