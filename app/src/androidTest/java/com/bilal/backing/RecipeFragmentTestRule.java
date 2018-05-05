package com.bilal.backing;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bilal.backing.activities.RecipeDetailActivity;
import com.bilal.backing.fragments.StepsFragment;
import com.bilal.backing.models.Recipe;

public class RecipeFragmentTestRule<F extends Fragment> extends ActivityTestRule<RecipeDetailActivity> {

    private StepsFragment mFragment;
    private Recipe recipe;

    public RecipeFragmentTestRule(Recipe recipe) {
        super(RecipeDetailActivity.class, true, false);
        this.recipe = recipe;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = StepsFragment.newInstance(recipe, false);
                transaction.replace(R.id.fr_recipe_detail, mFragment);
                transaction.commit();

            }

        });
    }

    public StepsFragment getFragment() {
        return mFragment;
    }
}