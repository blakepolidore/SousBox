package com.example.billy.sousbox.firebaseModels;

/**
 * Created by Billy on 5/10/16.
 */
public class UserLogin {

    private String[] facebookID;
    private Recipes[] recipes;


    public String[] getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String[] facebookID) {
        this.facebookID = facebookID;
    }

    public Recipes[] getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipes[] recipes) {
        this.recipes = recipes;
    }
}
