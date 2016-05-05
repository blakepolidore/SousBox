package com.example.billy.sousbox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.sousbox.Keys.Keys;
import com.example.billy.sousbox.R;
import com.example.billy.sousbox.adapters.CardAdapter;
import com.example.billy.sousbox.api.RecipeAPI;
import com.example.billy.sousbox.api.SpoonacularObjects;
import com.example.billy.sousbox.api.SpoonacularResults;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Billy on 5/4/16.
 */
public class SwipeItemActivity extends Fragment {



    private ArrayList<SpoonacularObjects> recipeLists;
    private CardAdapter adapter;
    private int i;

    RecipeAPI searchAPI;
    public final static String MASHAPLE_HEADER = Keys.getMASHAPLE();
    private String CHINESE = "chinese";
    private String foodType = "beef";
    private int OFFSET = 50;

    SwipeFlingAdapterView flingContainer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.swipe_activity_fragment, container, false);

        ButterKnife.inject(getActivity());
        setViews(v);
        recipeLists = new ArrayList<>();


        retrofitRecipe();
        adapter = new CardAdapter(getContext(), recipeLists);

        flingContainer.setAdapter(adapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)


                recipeLists.remove(0);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Timber.i("Saved");
                recipeLists.remove(0);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                recipeLists.remove(0);
                adapter.notifyDataSetChanged();
                Timber.i("Not like");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                //int moreRecipe = OFFSET *2;
               // moreRetrofitRecipePulling(moreRecipe);
//                if(itemsInAdapter == 29){
//                moreRetrofitRecipePulling();
//                }

                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {


                Timber.i("Clicked!");
            }
        });


    return v;
}

    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


    private void setViews(View v){
        flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frame);
    }



    private void retrofitRecipe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Call<SpoonacularResults> call = searchAPI.searchRecipe(foodType);
        call.enqueue(new Callback<SpoonacularResults>() {
            @Override
            public void onResponse(Call<SpoonacularResults> call, Response<SpoonacularResults> response) {
                SpoonacularResults spoonacularResults = response.body();


                if (spoonacularResults == null) {
                    return;
                }

                Collections.addAll(recipeLists, spoonacularResults.getResults());
                adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged(spoonacularResults.getResults());
            }

            @Override
            public void onFailure(Call<SpoonacularResults> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void moreRetrofitRecipePulling(int limit) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchAPI = retrofit.create(RecipeAPI.class);

        Call<SpoonacularResults> call = searchAPI.searchMoreRecipe(limit, foodType);
        call.enqueue(new Callback<SpoonacularResults>() {
            @Override
            public void onResponse(Call<SpoonacularResults> call, Response<SpoonacularResults> response) {
                SpoonacularResults spoonacularResults = response.body();


                if(spoonacularResults == null){
                    return;
                }

                Timber.i("pulling more listing");
                Collections.addAll(recipeLists, spoonacularResults.getResults());
                adapter.notifyDataSetChanged();
                //arrayAdapter.notifyDataSetChanged(spoonacularResults.getResults());
            }

            @Override
            public void onFailure(Call<SpoonacularResults> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

}
