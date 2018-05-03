package com.bilal.backing.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.activities.RecipeDetailActivity;
import com.bilal.backing.data.SharedPreferenceUtils;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.service.IngredientWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.popup_ingredients);
        views.setTextColor(R.id.tv_title, context.getResources().getColor(R.color.colorAccent));
        loadIngredientsData(views, context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void loadIngredientsData(RemoteViews rv, Context context) {
        Recipe favoriteRecipe = SharedPreferenceUtils.getFavoriteRecipe(context);
        if (favoriteRecipe != null) {
            String title = favoriteRecipe.getName() + " " + context.getString(R.string.ingredients);
            rv.setTextViewText(R.id.tv_title, title);
            Intent intent = new Intent(context, IngredientWidgetService.class);
            rv.setRemoteAdapter(R.id.lv_ingredients, intent);
            Intent detailsIntent = new Intent(context, RecipeDetailActivity.class);
            detailsIntent.putExtra(Utils.RECIPE, favoriteRecipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.lv_ingredients, pendingIntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

