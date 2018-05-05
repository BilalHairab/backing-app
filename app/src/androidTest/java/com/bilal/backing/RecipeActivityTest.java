package com.bilal.backing;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.bilal.backing.activities.RecipeDetailActivity;
import com.bilal.backing.fragments.StepsFragment;
import com.bilal.backing.models.Ingredient;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.models.Step;
import com.bilal.backing.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    @Rule
    public RecipeFragmentTestRule<StepsFragment> recipeDetailActivityTestRule;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private Recipe recipe;

    @Before
    public void setDefaults() {
        ingredients.add(new Ingredient(2.0f, "CUP", "rice"));
        steps.add(new Step(0, "short", "desc", "video", "thumb"));
        steps.add(new Step(1, "short1", "desc1", "video1", "thumb1"));
        recipe = new Recipe(0, "Name", ingredients, steps, 4, "");
        recipeDetailActivityTestRule = new RecipeFragmentTestRule<>(recipe);
        Intent intent = new Intent();
        intent.putExtra(Utils.RECIPE, recipe);
        recipeDetailActivityTestRule.launchActivity(intent);
    }

    @Test
    public void showToast_click_menuItem() {
        RecipeDetailActivity activity = recipeDetailActivityTestRule.getActivity();
        onView(withId(R.id.ic_favorite))
                .perform(click());

        String toastMessage = "";

        if (recipe != null) {
            toastMessage = recipe.getName() + " ";
            if (!activity.isFavorite) {
                toastMessage += activity.getString(R.string.un_favorite_message);
            } else {
                toastMessage += activity.getString(R.string.favorite_message);
            }
            onView(withText(toastMessage)).
                    inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                    check(matches(isDisplayed()));

        } else {
            onView(withText(toastMessage)).
                    inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
                    check(matches(not(isDisplayed())));
        }
    }

//    @Test
//    public void recipeNull_display_emptyFragment() {
//        recipeDetailActivityTestRule.launchActivity(null);
//        onView(withId(R.id.btn_ingredients)).check(matches(isDisplayed()));
//    }

}
