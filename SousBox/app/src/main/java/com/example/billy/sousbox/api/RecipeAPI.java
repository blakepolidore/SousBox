package com.example.billy.sousbox.api;

import com.example.billy.sousbox.Keys.Keys;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Billy on 4/29/16.
 */
public interface RecipeAPI {

    @GET("search?key=" + Keys.FOOD2FORK)
    Call<FoodTwoForkObjects> pullRecipe();

    @GET("search?cuisine={q}&diet=vegetarian&excludeIngredients=coconut&intolerances=egg%2C+gluten&limitLicense=false&number=10&offset=0&query=burger&type=main+course")
    Call<FoodTwoForkObjects> searchRecipe(@Query("q") String cuisine);

    @GET("X-Mashape-Key")
    Call<Keys> getKeys(@Header(Keys.MASHAPLE) String authorization);


}
