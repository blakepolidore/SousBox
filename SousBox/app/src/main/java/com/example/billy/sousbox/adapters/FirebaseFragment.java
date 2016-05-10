package com.example.billy.sousbox.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.billy.sousbox.R;
import com.example.billy.sousbox.firebaseModels.Recipes;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.core.view.View;
import com.firebase.ui.FirebaseRecyclerAdapter;

/**
 * Created by Billy on 5/10/16.
 */
public class FirebaseFragment extends Fragment {

    RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Recipes, FirebaseRecipeVIewHolder> mAdapter;
    private Query mRef;


    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.recycleview_custom_layout, container, false);
        setRetainInstance(true);
        setViews(v);
        initFirebase();


        mAdapter = new FirebaseRecyclerAdapter<Recipes, FirebaseRecipeVIewHolder>(Recipes.class, R.layout.recycleview_custom_layout, FirebaseRecipeVIewHolder.class, mRef) {
            @Override
            public void populateViewHolder(FirebaseRecipeVIewHolder holder, Recipes recipes, final int position) {
                String titleNa = recipes.getTitle();



            }
        };
        recyclerView.setAdapter(mAdapter);


        return v;
    }

    private void initFirebase(){
        String facebookUserID = getAuthData();
        Firebase ref = new Firebase("https://sous-box.firebaseio.com/users/" + facebookUserID);
        mRef = ref.child("recipes");
    }

    public void setViews(android.view.View v){
        recyclerView = (RecyclerView)v.findViewById(R.id.recipeLists_recycleView_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    private String getAuthData() {
        Firebase firebase = new Firebase("https://sous-box.firebaseio.com");
        AuthData authData = firebase.getAuth();
        String uID = authData.getUid();
        return uID;
    }
}