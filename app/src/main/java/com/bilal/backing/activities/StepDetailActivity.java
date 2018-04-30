package com.bilal.backing.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.fragments.StepDetailFragment;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.models.Step;

public class StepDetailActivity extends AppCompatActivity implements OnStepChanged {
    Recipe mRecipe;
    int lastPosition = -1;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null && savedInstanceState.containsKey(Utils.RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(Utils.RECIPE);
            lastPosition = savedInstanceState.getInt(Utils.STEP_SEQUENCE);
//            changeStep(lastPosition);
        } else if (getIntent().hasExtra(Utils.STEP_SEQUENCE)) {
            mRecipe = getIntent().getExtras().getParcelable(Utils.RECIPE);
            lastPosition = getIntent().getExtras().getInt(Utils.STEP_SEQUENCE);
            changeStep(lastPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null) {
            outState.putParcelable(Utils.RECIPE, mRecipe);
            outState.putInt(Utils.STEP_SEQUENCE, lastPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNextStep() {
        if (mRecipe != null) {
            lastPosition++;
            changeStep(lastPosition);
        }
    }

    @Override
    public void onChangeStep(int position) {

    }

    void changeStep(int toStep) {
        Step to = mRecipe.getSteps().get(toStep);
        setTitle(to.getShortDescription());
        fragmentManager.beginTransaction().replace(R.id.fr_step_detail,
                StepDetailFragment.newInstance(to,
                        toStep + 1 == mRecipe.getSteps().size())).commit();
    }
}
