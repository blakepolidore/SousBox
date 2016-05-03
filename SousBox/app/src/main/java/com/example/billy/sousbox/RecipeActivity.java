package com.example.billy.sousbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class RecipeActivity extends AppCompatActivity {

    private RecycleViewAdatper recycleAdapter;
    private RecyclerView recyclerView;
    private RecipeAPI foodRecipePulling;
    private ArrayList<SpoonacularObjects> recipeLists;
    private int numberOfRecipe = 30;
    private RecipeAPI searchAPI;
    private String BEEF = "beef";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        setViews();

        recipeLists = new ArrayList<>();


        retrofitRecipe();

        recycleAdapter = new RecycleViewAdatper(recipeLists);
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    private void retrofitRecipe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Call<SpoonacularResults> call = searchAPI.searchRecipe(BEEF);
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
                }

                //arrayAdapter.notifyDataSetChanged(spoonacularResults.getResults());
            }

            @Override
            public void onFailure(Call<SpoonacularResults> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }


    private void setViews(){
        recyclerView = (RecyclerView) findViewById(R.id.saved_recipeView_recycleView_id);

    }


}
