package com.example.billy.sousbox.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.sousbox.R;
import com.example.billy.sousbox.adapters.RecycleViewAdatper;
import com.example.billy.sousbox.api.QueryFilters;
import com.example.billy.sousbox.api.RecipeAPI;
import com.example.billy.sousbox.api.SpoonacularObjects;
import com.example.billy.sousbox.api.SpoonacularResults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Billy on 5/4/16.
 */
public class FoodListsMainFragment extends Fragment {

    private RecycleViewAdatper recycleAdapter;
    private RecyclerView recyclerView;
    private RecipeAPI foodRecipePulling;
    private ArrayList<SpoonacularObjects> recipeLists;
    public final static String RECIPE_ID_KEY = "recipeID";
    public final static String IMAGE_KEY = "image";
    private String querySearch;
    private int numberOfRecipe = 30;
    private RecipeAPI searchAPI;
    private String cuisine = "chinese";
    QueryFilters queryFilters;


    public void setQuerySearch(String querySearch) {
        this.querySearch = querySearch;
    }

    public String getQuerySearch() {
        return querySearch;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_recipe_recycleview, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recipeLists_recycleView_id);
        recipeLists = new ArrayList<>();
        //
        //querySearch = queryFilters.getQuery();
        Bundle filterBundle = getArguments();
        //String getType = filterBundle.getString(PreferencesFragment.FILTER_KEY);
        //querySearch = getType;
//        String getSerach = getQuerySearch();
//        querySearch = getSerach;

        querySearch = getSearchFilter();
        retrofitRecipe();
        recycleAdapter = new RecycleViewAdatper(recipeLists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleAdapterItemClicker();
        return v;
    }


    /**
     * get filter preferences from fragment
     */
    private String getSearchFilter(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferencesFragment.Shared_FILTER_KEY, "");
    }

    private void setFilterQuery(){

    }


    /**
     * Set the itemClicker for the recycleView
     *
     * bundle to pass the ID into the API ingredients called
     */
    private void recycleAdapterItemClicker() {
        recycleAdapter.setOnItemClickListener(new RecycleViewAdatper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Timber.i(String.valueOf(position));
                recipeLists.get(position);

                Bundle recipeId = new Bundle();
                int recipe = recipeLists.get(position).getId();
                String image = recipeLists.get(position).getImage();
                recipeId.putInt(RECIPE_ID_KEY, recipe);
                recipeId.putString(IMAGE_KEY, image);


                Fragment ingredients = new IngredientsFragment();
                ingredients.setArguments(recipeId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_id, ingredients);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }



    private void retrofitRecipe() {
       // String getSearch = queryFilters.getQuery();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Call<SpoonacularResults> call = searchAPI.searchRecipe(querySearch);
        call.enqueue(new Callback<SpoonacularResults>() {
            @Override
            public void onResponse(Call<SpoonacularResults> call, Response<SpoonacularResults> response) {
                SpoonacularResults spoonacularResults = response.body();

                if(spoonacularResults == null){
                    return;
                }

                Collections.addAll(recipeLists, spoonacularResults.getResults());

                if (recyclerView != null) {
                    long seed = System.nanoTime();
                    Collections.shuffle(recipeLists, new Random(seed));

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
}
