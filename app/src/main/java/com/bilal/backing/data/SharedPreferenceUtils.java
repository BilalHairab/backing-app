package com.bilal.backing.data;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.bilal.backing.R;
import com.bilal.backing.utils.Utils;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import static com.bilal.backing.utils.Utils.FAVORITE_RECIPE;

public class SharedPreferenceUtils {

    private static final String defaultResult = "default";

    public static boolean checkIfRecipeIsFavorite(Context context, Recipe recipe) {
        String recipeString = recipeToString(recipe);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.FAVORITES, Context.MODE_PRIVATE);
        return (sharedPreferences.getString(FAVORITE_RECIPE, defaultResult).contentEquals(recipeString));
    }

    public static void setFavoriteRecipe(Context context, Recipe recipe) {
        String recipeString = recipeToString(recipe);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.FAVORITES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVORITE_RECIPE, recipeString);
        editor.apply();
        updateWidget(context);
    }

    public static Recipe getFavoriteRecipe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.FAVORITES, Context.MODE_PRIVATE);
        String recipeString = sharedPreferences.getString(FAVORITE_RECIPE, defaultResult);
        if (recipeString.contentEquals(defaultResult))
            return null;
        else return stringToRecipe(recipeString);
    }

    public static void removeFavoriteRecipe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.FAVORITES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVORITE_RECIPE, defaultResult);
        editor.apply();
        updateWidget(context);
    }

    private static String recipeToString(Recipe recipe) {
        Gson gson = new Gson();
        return gson.toJson(recipe);
    }

    private static Recipe stringToRecipe(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, Recipe.class);
    }

    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.layout.popup_ingredients);
        for (int id : widgetIds)
            RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, id);

    }

}
