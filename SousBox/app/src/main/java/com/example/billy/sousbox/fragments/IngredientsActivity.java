package com.example.billy.sousbox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.billy.sousbox.R;
import com.example.billy.sousbox.api.GetRecipeObjects;
import com.example.billy.sousbox.api.RecipeAPI;
import com.example.billy.sousbox.api.SpoonGetRecipe;
import com.example.billy.sousbox.api.SpoonacularObjects;
import com.example.billy.sousbox.api.SpoonacularResults;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Billy on 5/3/16.
 */
public class IngredientsActivity extends Fragment {

    ArrayList<String> ingredientLists;
    ArrayAdapter adapter;
    private RecipeAPI searchAPI;
    int id;
    String image;
    ListView ingredientsLV;
    TextView title;
    ImageView recipeImage;
    GetRecipeObjects getRecipeObjects;
    SpoonacularObjects spoonacularObjects;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ingredients_layout_fragment, container, false);

        recipeImage = (ImageView) v.findViewById(R.id.ingredients_imageView_id);
        title = (TextView) v.findViewById(R.id.ingredients_titleView_id);
        ingredientsLV = (ListView)v.findViewById(R.id.ingredients_listView_id);


        retrofitRecipeID();
        return v;
    }




    private void retrofitRecipeID() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Bundle bundle = getArguments();
        id = bundle.getInt(FoodListsMainActivity.RECIPEID_KEY);
        image = bundle.getString(FoodListsMainActivity.IMAGE_KEY);


        Call<GetRecipeObjects> call = searchAPI.getRecipeIngredients(id);
        call.enqueue(new Callback<GetRecipeObjects>() {
            @Override
            public void onResponse(Call<GetRecipeObjects> call, Response<GetRecipeObjects> response) {
                getRecipeObjects = response.body();


                if (getRecipeObjects == null) {
                    return;
                }

                title.setText(getRecipeObjects.getTitle());

                String imageURI = image;
                if (imageURI.isEmpty()) {
                    imageURI = "R.drawable.blank_white.png";
                }
                Picasso.with(getContext())
                        .load("https://webknox.com/recipeImages/" + imageURI)
                        .resize(500, 500)
                        .centerCrop()
                        .into(recipeImage);

                ingredientLists = new ArrayList<>();

                SpoonGetRecipe[] recipe = getRecipeObjects.getExtendedIngredients();
                for(int i = 0; i < recipe.length; i++ ){
                    ingredientLists.add(recipe[i].getOriginalString());
                }
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ingredientLists);
                ingredientsLV.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetRecipeObjects> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
}
