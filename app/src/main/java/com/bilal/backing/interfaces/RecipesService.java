package com.bilal.backing.interfaces;

import com.bilal.backing.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesService {
    @GET("baking.json")
    Call<ArrayList<Recipe>> listRecipes();
}
