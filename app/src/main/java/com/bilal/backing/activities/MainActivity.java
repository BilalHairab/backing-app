package com.bilal.backing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.adapters.RecipesAdapter;
import com.bilal.backing.idling.MainActivityIdlingResource;
import com.bilal.backing.interfaces.OnRecipeSelected;
import com.bilal.backing.interfaces.RecipesService;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.views.MyRecyclerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnRecipeSelected {

    @BindView(R.id.rv_recipes)
    RecyclerView recyclerView;

    public ArrayList<Recipe> recipes;
    LinearLayoutManager linearLayoutManager;
    static final String STEPS_STATE = "steps_state";
    Parcelable stepsState;
    @Nullable
    private MainActivityIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recipes = new ArrayList<>();
        if (savedInstanceState != null && savedInstanceState.containsKey(Utils.RECIPES)) {
            recipes = savedInstanceState.getParcelableArrayList(Utils.RECIPES);
            adaptRecipes(recipes);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RecipesService service = retrofit.create(RecipesService.class);
            Call<ArrayList<Recipe>> recipesCall = service.listRecipes();
            recipesCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                    recipes.clear();
                    recipes = response.body();
                    adaptRecipes(recipes);
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                    Log.e("bilal_recipes", t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (recipes.size() > 0) {
            outState.putParcelableArrayList(Utils.RECIPES, recipes);
            outState.putParcelable(STEPS_STATE, linearLayoutManager.onSaveInstanceState());
        }
        super.onSaveInstanceState(outState);
    }

    void adaptRecipes(ArrayList<Recipe> recipeArrayList) {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        MyRecyclerItemDecoration itemDecoration = new MyRecyclerItemDecoration(9, 4);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecipesAdapter adapter = new RecipesAdapter(recipeArrayList, this);
        recyclerView.setAdapter(adapter);
        if (stepsState != null)
            linearLayoutManager.onRestoreInstanceState(stepsState);
    }

    @Override
    public void onRecipeChoose(int position) {
        Intent x = new Intent(MainActivity.this, RecipeDetailActivity.class);
        x.putExtra(Utils.RECIPE, recipes.get(position));
        startActivity(x);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new MainActivityIdlingResource();
        }
        return mIdlingResource;
    }
}
