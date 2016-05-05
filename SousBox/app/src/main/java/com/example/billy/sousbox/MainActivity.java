package com.example.billy.sousbox;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.billy.sousbox.Keys.Keys;
import com.example.billy.sousbox.adapters.CardAdapter;
import com.example.billy.sousbox.api.SpoonacularObjects;
import com.example.billy.sousbox.api.SpoonacularResults;
//import com.example.billy.sousbox.flingsswipe.SwipeFlingAdapterView;
import com.example.billy.sousbox.api.RecipeAPI;
import com.example.billy.sousbox.fragments.LoginActivityFragment;
import com.example.billy.sousbox.fragments.RecipeListsMainActivity;
import com.example.billy.sousbox.fragments.SwipeItemActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
//import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FrameLayout fragContainer;
    private RecipeListsMainActivity recipeListsFrag;
    private LoginActivityFragment faceBookLoginFrag;
    private SwipeItemActivity swipeItemActivityFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        initiViews();
        initiFragment();
        bottomNavi();


    }

    private void initiFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_id, recipeListsFrag);
        fragmentTransaction.commit();

    }


    private void initiViews(){
        fragContainer = (FrameLayout)findViewById(R.id.fragment_container_id);
        recipeListsFrag = new RecipeListsMainActivity();
        faceBookLoginFrag = new LoginActivityFragment();
        fragmentManager = getSupportFragmentManager();


    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bottomNavi(){
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_menu_gallery, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_menu_share, R.color.colorPrimaryDark);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_menu_manage, R.color.colorAccent);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

// Set listener
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {

                if(position ==0) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_id, recipeListsFrag);
                    fragmentTransaction.commit();
                }

                if(position ==1) {
                    swipeItemActivityFrag = new SwipeItemActivity();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_id, swipeItemActivityFrag);
                    fragmentTransaction.commit();

                }
                if(position ==2) {
                    faceBookLoginFrag = new LoginActivityFragment();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_id, faceBookLoginFrag);
                    fragmentTransaction.commit();

                }

                // Do something cool here...
            }
        });

    }

}
