package com.example.billy.sousbox.food2forkapi;

/**
 * Created by Billy on 4/29/16.
 */
public class FoodTwoArray {
    private FoodTwoForkObjects[] recipes;
    private int counts;

    public FoodTwoArray(FoodTwoForkObjects[] recipes, int counts) {
        this.recipes = recipes;
        this.counts = counts;
    }

    public FoodTwoForkObjects[] getRecipes() {
        return recipes;
    }

    public void setRecipes(FoodTwoForkObjects[] recipes) {
        this.recipes = recipes;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }
}
