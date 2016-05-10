package com.example.billy.sousbox.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.billy.sousbox.R;
import com.example.billy.sousbox.adapters.RecycleViewAdapter;

/**
 * Created by Billy on 5/2/16.
 */
public class SavedRecipeFragment extends Fragment {

    private RecycleViewAdapter recycleAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.food_recipe_recycleview, container, false);



        return v;
    }

}
