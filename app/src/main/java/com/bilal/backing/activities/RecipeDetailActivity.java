package com.bilal.backing.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.fragments.StepDetailFragment;
import com.bilal.backing.fragments.StepsFragment;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements OnStepChanged {
    Recipe mRecipe;
    boolean deviceIsTablet = false;
    FragmentManager fragmentManager;
    int lastPosition = -1;
    OnStepChanged onStepChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();
        onStepChanged = RecipeDetailActivity.this;
        if (findViewById(R.id.fr_step_detail) != null)
            deviceIsTablet = true;

        if (savedInstanceState != null && savedInstanceState.containsKey(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "saved");
            mRecipe = savedInstanceState.getParcelable(Utils.RECIPE);
            setTitle(mRecipe.getName());
        } else if (getIntent().hasExtra(Utils.RECIPE)) {
            Log.e("bilal_onCreate", "intent");
            mRecipe = configureRecipeData(getIntent().getExtras());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null)
            outState.putParcelable(Utils.RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
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
        fragmentManager.beginTransaction().replace(R.id.fr_step_detail,
                StepDetailFragment.newInstance(mRecipe.getSteps().get(toStep),
                        toStep + 1 == mRecipe.getSteps().size())).commit();
    }

    Recipe configureRecipeData(Bundle bundle) {
        Recipe recipe = bundle.getParcelable(Utils.RECIPE);
        setTitle(recipe.getName());
        fragmentManager.beginTransaction().replace(R.id.fr_recipe_detail, StepsFragment.newInstance(recipe,
                deviceIsTablet)).commit();
        return recipe;
    }
}
