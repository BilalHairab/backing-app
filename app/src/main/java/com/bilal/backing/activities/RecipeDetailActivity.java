package com.bilal.backing.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.data.SharedPreferenceUtils;
import com.bilal.backing.fragments.StepDetailFragment;
import com.bilal.backing.fragments.StepsFragment;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.models.Step;

public class RecipeDetailActivity extends AppCompatActivity implements OnStepChanged {
    Recipe mRecipe;
    boolean deviceIsTablet = false;
    FragmentManager fragmentManager;
    int lastPosition = -1;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.fr_step_detail) != null)
            deviceIsTablet = true;
        if (savedInstanceState != null && savedInstanceState.containsKey(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "saved");
            mRecipe = savedInstanceState.getParcelable(Utils.RECIPE);
        } else if (getIntent().hasExtra(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "intent");
            mRecipe = getIntent().getExtras().getParcelable(Utils.RECIPE);
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
        String toastMessage = mRecipe.getName() + " ";
        switch (item.getItemId()) {
            case R.id.ic_favorite:
                if (isFavorite) {
                    SharedPreferenceUtils.removeFavoriteRecipe(this);
                    item.setIcon(R.drawable.ic_favorite);
                    toastMessage += getString(R.string.un_favorite_message);
                } else {
                    SharedPreferenceUtils.setFavoriteRecipe(this, mRecipe);
                    item.setIcon(R.drawable.ic_unfavorite);
                    toastMessage += getString(R.string.favorite_message);
                }
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
                isFavorite = !isFavorite;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecipe != null)
            configureRecipeData();
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
