package com.bilal.backing.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.adapters.RecipesAdapter;
import com.bilal.backing.interfaces.RecipesService;
import com.bilal.backing.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gv_recipes)
    GridView gridView;

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipesService service = retrofit.create(RecipesService.class);
        Call<List<Recipe>> recipesCall = service.listRecipes();
        recipesCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                recipes = response.body();
                RecipesAdapter adapter = new RecipesAdapter(MainActivity.this, recipes);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent x = new Intent(MainActivity.this, RecipeDetailActivity.class);
                        x.putExtra(Utils.RECIPE, recipes.get(position));
                        startActivity(x);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e("bilal_recipes", t.getMessage());
            }
        });
    }
}
