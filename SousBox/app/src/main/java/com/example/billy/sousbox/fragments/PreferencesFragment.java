package com.example.billy.sousbox.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.preference.PreferenceManager;
import com.example.billy.sousbox.R;
import com.example.billy.sousbox.api.QueryFilters;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.util.Map;
import timber.log.Timber;

/**
 * Created by Billy on 5/2/16.
 */
public class PreferencesFragment extends Fragment {



    private TextView info;
    private Button saveButton;
    /* The login button for Facebook */
    /* The callback manager for Facebook */
    /* Used to track user logging in/out off Facebook */
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker mFacebookAccessTokenTracker;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FrameLayout fragContainer;
    private FoodListsMainFragment recipeListsFrag;
    private ProgressDialog mAuthProgressDialog;
    /* A reference to the Firebase */
    private Firebase mFirebaseRef;
    /* Data from the authenticated user */
    private AuthData mAuthData;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;

    public static CheckBox beefCheckBox, porkCheckBox, chickenCheckBox, vegetarianCheckBox, seafoodCheckbox, allTypeCheckBox;
    SharedPreferences sharedPreferences;

    QueryFilters queryFilters;
    //Bundle filterBundle;

    //region Checked booleans
    private boolean beefCheck = false;
    private boolean porkCheck = false;
    private boolean chickenCheck = false;
    private boolean vegetarianCheck = false;
    private boolean seafoodCheck = false;
    private boolean allTypeCheck = false;
    //endregion Checked booleans

    // region Shared Preferences Booleans Codes
    private String BEEF_CODE = "beef";
    private String PORK_CODE = "pork";
    private String CHICKEN_CODE = "chicken";
    private String VEGETARIAN_CODE = "vegetarian";
    private String SEAFOOD_CODE = "seafood";
    private String ALL_TYPE_CODE = "allType";
    //endregion Shared Preferences Booleans Codes

    public final static String FILTER_KEY = "filter";
    public final static String Shared_FILTER_KEY = "shared filter";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preferences_layout_fragment, container, false);
        FacebookSdk.sdkInitialize(v.getContext());
        callbackManager = CallbackManager.Factory.create();
/*
        mAuthProgressDialog = new ProgressDialog(getContext());
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        *//* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons *//*
        mFirebaseRef.addAuthStateListener(mAuthStateListener);*/

        initiViews(v);
        facebookLogin();
        recipeListsFrag = new FoodListsMainFragment();
        queryFilters = new QueryFilters();
        initiCheckboxClicks();

        return v;
    }



    private void initiViews(View v){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        loginButton = (LoginButton)v.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        info = (TextView)v.findViewById(R.id.info);
        saveButton = (Button)v.findViewById(R.id.filter_save_button_id);

        fragContainer = (FrameLayout)v.findViewById(R.id.fragment_container_id);
        fragmentManager = getFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        beefCheckBox = (CheckBox)v.findViewById(R.id.beef_checkbox_id);
        porkCheckBox = (CheckBox)v.findViewById(R.id.pork_checkbox_id);
        chickenCheckBox = (CheckBox)v.findViewById(R.id.chicken_checkbox_id);
        seafoodCheckbox = (CheckBox)v.findViewById(R.id.seafood_checkout_id);
        vegetarianCheckBox = (CheckBox)v.findViewById(R.id.vege_checkout_id);
        allTypeCheckBox = (CheckBox)v.findViewById(R.id.allType_checkbox_id);

    }



    private void onCheckboxClicked(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((CheckBox) view).isChecked();

                switch (view.getId()) {
                    case R.id.beef_checkbox_id:
                        if (checked) {
                            beefCheck = true;
                            String beefFilter = "beef";
                            setSearchFilter(beefFilter);
                        } else {
                            beefCheck = false;
                            setSearchFilter("");
                        }
                        break;
                    case R.id.chicken_checkbox_id:
                        if (checked) {
                            chickenCheck = true;
//                            Bundle filterBundle = new Bundle();
                            String chickenFilter = "chicken";
                            setSearchFilter(chickenFilter);
//                            recipeListsFrag.setQuerySearch(chickenFilter);
                        } else {
                            chickenCheck = false;
                            setSearchFilter("");
                        }
                        break;
                    case R.id.pork_checkbox_id:
                        if (checked) {
                            porkCheck = true;
                            String porkFilter = "pork";
                            setSearchFilter(porkFilter);
                        } else {
                            porkCheck = false;
                            setSearchFilter("");
                        }
                        break;
                    case R.id.vege_checkout_id:
                        if (checked) {
                            vegetarianCheck = true;
                            String vegeFilter = "vegetarian";
                            setSearchFilter(vegeFilter);
                        } else {
                            vegetarianCheck = false;
                            setSearchFilter("");
                        }
                        break;
                    case R.id.seafood_checkout_id:
                        if (checked) {
                            seafoodCheck = true;
                            String seafoodFilter = "seafood";
                            setSearchFilter(seafoodFilter);
                        } else {
                            seafoodCheck = false;
                            setSearchFilter("");
                        }
                        break;

                    case R.id.allType_checkbox_id:
                        if (checked) {
                            allTypeCheck = true;
                            String allTypeFilter = "";
                            setSearchFilter(allTypeFilter);
                        } else {
                            allTypeCheck = false;
                            setSearchFilter("");
                        }
                        break;
                    default:
                        setSearchFilter("");
                }
            }
        });
    }

    private void setSearchFilter(String filterName){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Shared_FILTER_KEY, filterName);
        editor.commit();
    }

    /**
     * Adds each checkbox to see if they have been checked
     */
    private void initiCheckboxClicks() {
        onCheckboxClicked(beefCheckBox);
        onCheckboxClicked(porkCheckBox);
        onCheckboxClicked(chickenCheckBox);
        onCheckboxClicked(vegetarianCheckBox);
        onCheckboxClicked(allTypeCheckBox);
        onCheckboxClicked(seafoodCheckbox);
    }

    /**
     * Saves the state of the checkboxes in the shared preferences
     */
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BEEF_CODE, beefCheck);
        editor.putBoolean(PORK_CODE, porkCheck);
        editor.putBoolean(CHICKEN_CODE, chickenCheck);
        editor.putBoolean(ALL_TYPE_CODE, allTypeCheck);
        editor.putBoolean(SEAFOOD_CODE, seafoodCheck);
        editor.putBoolean(VEGETARIAN_CODE, vegetarianCheck);
        editor.commit();
    }


    /**
     * Retrieves the state of the checkboxes from the shared preferences
     */
    @Override
    public void onResume() {
        super.onResume();
        porkCheck = sharedPreferences.getBoolean(PORK_CODE, porkCheck);
        porkCheckBox.setChecked(porkCheck);
        beefCheck = sharedPreferences.getBoolean(BEEF_CODE, beefCheck);
        beefCheckBox.setChecked(beefCheck);
        chickenCheck = sharedPreferences.getBoolean(CHICKEN_CODE, chickenCheck);
        chickenCheckBox.setChecked(chickenCheck);
        seafoodCheck = sharedPreferences.getBoolean(SEAFOOD_CODE, seafoodCheck);
        seafoodCheckbox.setChecked(seafoodCheck);
        vegetarianCheck = sharedPreferences.getBoolean(VEGETARIAN_CODE, vegetarianCheck);
        vegetarianCheckBox.setChecked(vegetarianCheck);
        allTypeCheck = sharedPreferences.getBoolean(ALL_TYPE_CODE, allTypeCheck);
        allTypeCheckBox.setChecked(allTypeCheck);

    }

    private void facebookLogin(){

        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Timber.i("Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                PreferencesFragment.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText("User ID: " + loginResult.getAccessToken().getUserId()
                        //+ "\n" +
                        // "Auth Token: "
                        // + loginResult.getAccessToken().getToken()
                );

//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_id, recipeListsFrag);
//                fragmentTransaction.commit();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }



    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFirebaseRef.unauth();
                //setAuthenticatedUser(null);
            }
        }
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }

    /**
     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
     * necessary parameters depending on the provider)
     */
    private void authWithFirebase(final String provider, Map<String, String> options) {
        if (options.containsKey("error")) {
            showErrorDialog(options.get("error"));
        } else {
            mAuthProgressDialog.show();
            if (provider.equals("twitter")) {
                // if the provider is twitter, we pust pass in additional options, so use the options endpoint
                mFirebaseRef.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
            } else {
                // if the provider is not twitter, we just need to pass in the oauth_token
                mFirebaseRef.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
            }
        }
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
            loginButton.setVisibility(View.GONE);

            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")) {
                name = (String) authData.getProviderData().get("displayName");
            }
            else {
                Timber.i("Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
//                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
            }
        } else {
            /* No authenticated user show all the login buttons */
            loginButton.setVisibility(View.VISIBLE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */

    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Timber.i(" " + provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    private void loginAnonymously() {
        mAuthProgressDialog.show();
        mFirebaseRef.authAnonymously(new AuthResultHandler("anonymous"));
    }

}
