package com.bilal.backing.interfaces;

import com.bilal.backing.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesService {
    @GET("baking.json")
    Call<List<Recipe>> listRecipes();
}
