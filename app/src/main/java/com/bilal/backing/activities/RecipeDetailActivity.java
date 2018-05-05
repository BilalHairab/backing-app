package com.bilal.backing.activities;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.bilal.backing.R;
import com.bilal.backing.data.SharedPreferenceUtils;
import com.bilal.backing.fragments.StepDetailFragment;
import com.bilal.backing.fragments.StepsFragment;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.models.Step;
import com.bilal.backing.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements OnStepChanged {
    @VisibleForTesting
    public Recipe mRecipe;
    boolean deviceIsTablet = false;
    FragmentManager fragmentManager;
    int lastPosition = -1;
    @VisibleForTesting
    public boolean isFavorite = false;

    @BindView(R.id.layout_recipe)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.fr_step_detail) != null)
            deviceIsTablet = true;
        if (savedInstanceState != null && savedInstanceState.containsKey(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "saved");
            mRecipe = savedInstanceState.getParcelable(Utils.RECIPE);
        } else if (getIntent().hasExtra(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "intent");
            mRecipe = getIntent().getExtras().getParcelable(Utils.RECIPE);
            configureRecipeData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null)
            outState.putParcelable(Utils.RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_menu, menu);
        MenuItem item = menu.getItem(0);
        isFavorite = SharedPreferenceUtils.checkIfRecipeIsFavorite(this, mRecipe);
        if (isFavorite)
            item.setIcon(R.drawable.ic_unfavorite);
        else item.setIcon(R.drawable.ic_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_favorite:
                if (mRecipe != null) {
                    String snackMessage = mRecipe.getName() + " ";
                    if (isFavorite) {
                        SharedPreferenceUtils.removeFavoriteRecipe(this);
                        item.setIcon(R.drawable.ic_favorite);
                        snackMessage += getString(R.string.un_favorite_message);
                    } else {
                        SharedPreferenceUtils.setFavoriteRecipe(this, mRecipe);
                        item.setIcon(R.drawable.ic_unfavorite);
                        snackMessage += getString(R.string.favorite_message);
                    }
                    isFavorite = !isFavorite;
                    Snackbar snackbar = Snackbar.make(linearLayout, snackMessage, Snackbar.LENGTH_LONG);
                    snackbar.show();
//                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
                    break;
                }
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    /*
     * Note: Up - Back buttons was removing fragment transactions instead of popping the last activity in the app
     * so I had to force both actions to back in activity
     */
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 2 || fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onNextStep() {
        if (mRecipe != null && deviceIsTablet) {
            lastPosition++;
            changeStep(lastPosition);
        }
    }

    @Override
    public void onChangeStep(int position) {
        if (mRecipe != null && deviceIsTablet) {
            lastPosition = position;
            changeStep(lastPosition);
        }
    }

    void changeStep(int toStep) {
        Step step = mRecipe.getSteps().get(toStep);
        setTitle(step.getShortDescription());
        Fragment fragment = fragmentManager.findFragmentByTag(Utils.STEP_FRAGMENT_TAG);
        if (fragment != null) {
            fragmentManager.popBackStackImmediate(Utils.STEP_FRAGMENT_TAG, 0);
            return;
        }
        fragment = StepDetailFragment.newInstance(mRecipe.getSteps().get(toStep), toStep + 1 == mRecipe.getSteps().size());
        fragmentManager.beginTransaction().replace(R.id.fr_step_detail, fragment).commit();
    }

    void configureRecipeData() {
        setTitle(mRecipe.getName());
        Fragment fragment = fragmentManager.findFragmentByTag(Utils.RECIPES);
        if (fragment != null) {
            fragmentManager.popBackStackImmediate(Utils.RECIPES, 0);
            return;
        }
        fragment = StepsFragment.newInstance(mRecipe, deviceIsTablet);
        fragmentManager.beginTransaction().replace(R.id.fr_recipe_detail, fragment).addToBackStack(Utils.RECIPES).commit();
    }
}
