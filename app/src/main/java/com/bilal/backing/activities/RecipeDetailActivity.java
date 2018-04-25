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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fr_step_detail) != null)
            deviceIsTablet = true;
        if (getIntent().hasExtra(Utils.RECIPE)) {
            mRecipe = (Recipe) getIntent().getExtras().get(Utils.RECIPE);
            setTitle(mRecipe.getName());
            Log.e("bilal_step", "received ");
            fragmentManager.beginTransaction().replace(R.id.fr_recipe_detail, StepsFragment.newInstance(mRecipe,
                    deviceIsTablet, this)).commit();
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
        fragmentManager.beginTransaction().replace(R.id.fr_step_detail,
                StepDetailFragment.newInstance(mRecipe.getSteps().get(toStep),
                        toStep + 1 == mRecipe.getSteps().size(), this)).commit();
    }
}
