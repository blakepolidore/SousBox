package com.example.billy.sousbox.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.sousbox.R;
import com.example.billy.sousbox.adapters.RecycleViewAdatper;
import com.example.billy.sousbox.api.RecipeAPI;
import com.example.billy.sousbox.api.SpoonacularObjects;
import com.example.billy.sousbox.api.SpoonacularResults;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Billy on 5/4/16.
 */
public class FoodListsMainActivity extends Fragment {

    private RecycleViewAdatper recycleAdapter;
    private RecyclerView recyclerView;
    private RecipeAPI foodRecipePulling;
    private ArrayList<SpoonacularObjects> recipeLists;
    public final static String RECIPEID_KEY = "recipeID";
    public final static String IMAGE_KEY = "image";

    private int numberOfRecipe = 30;
    private RecipeAPI searchAPI;
    private String BEEF = "beef";
    private String PORK = "pork";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_recipe_recycleview, container, false);


        setViews(v);

        recipeLists = new ArrayList<>();
        retrofitRecipe();
        recycleAdapter = new RecycleViewAdatper(recipeLists);
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleAdapterItemClicker();

        return v;
    }


    /**
     * Set the itemClicker for the recycleView
     */
    private void recycleAdapterItemClicker() {
        recycleAdapter.setOnItemClickListener(new RecycleViewAdatper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Timber.i(String.valueOf(position));
                recipeLists.get(position);

                Bundle recipeId = new Bundle(); //will bundle the 5 fields of newsWireObjects in a string array
                int recipe = recipeLists.get(position).getId();
                String image = recipeLists.get(position).getImage();
                recipeId.putInt(RECIPEID_KEY, recipe);
                recipeId.putString(IMAGE_KEY, image);


                Fragment ingredients = new IngredientsActivity();
                ingredients.setArguments(recipeId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_id, ingredients);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }



    private void retrofitRecipe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Call<SpoonacularResults> call = searchAPI.searchRecipe(PORK);
        call.enqueue(new Callback<SpoonacularResults>() {
            @Override
            public void onResponse(Call<SpoonacularResults> call, Response<SpoonacularResults> response) {
                SpoonacularResults spoonacularResults = response.body();


                if(spoonacularResults == null){
                    return;
                }

                Collections.addAll(recipeLists, spoonacularResults.getResults());

                if (recyclerView != null) {
                    recyclerView.setAdapter(recycleAdapter);
                    recycleAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<SpoonacularResults> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }


    private void setViews(View v){
        recyclerView = (RecyclerView) v.findViewById(R.id.recipeLists_recycleView_id);

    }



}
