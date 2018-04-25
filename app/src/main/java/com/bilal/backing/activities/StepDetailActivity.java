package com.bilal.backing.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.fragments.StepDetailFragment;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Recipe;

public class StepDetailActivity extends AppCompatActivity implements OnStepChanged {
    Recipe mRecipe;
    int lastPosition = -1;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        fragmentManager = getSupportFragmentManager();
        if (getIntent().hasExtra(Utils.STEP_SEQUENCE)) {
            mRecipe = getIntent().getExtras().getParcelable(Utils.RECIPE);
            lastPosition = getIntent().getExtras().getInt(Utils.STEP_SEQUENCE);
            changeStep(lastPosition);
        }
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
        fragmentManager.beginTransaction().replace(R.id.fr_step_detail,
                StepDetailFragment.newInstance(mRecipe.getSteps().get(toStep),
                        toStep + 1 == mRecipe.getSteps().size(), this)).commit();
    }

}
