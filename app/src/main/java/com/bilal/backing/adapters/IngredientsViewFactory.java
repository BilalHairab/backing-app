package com.bilal.backing.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.activities.RecipeDetailActivity;
import com.bilal.backing.data.SharedPreferenceUtils;
import com.bilal.backing.models.Ingredient;
import com.bilal.backing.models.Recipe;

public class IngredientsViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Recipe recipe;

    public IngredientsViewFactory(Context context) {
        this.context = context;
        Log.e("bilal_Widget", "Constructor");

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = SharedPreferenceUtils.getFavoriteRecipe(context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipe == null)
            return 0;
        else return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipe != null) {
            Log.e("bilal_Widget", "getViewAt");
            Ingredient ingredient = recipe.getIngredients().get(position);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
            String quantity = ingredient.getQuantity() + " " + ingredient.getMeasure();
            rv.setTextViewText(R.id.tv_ingredient_name, ingredient.getIngredient());
            rv.setTextViewText(R.id.tv_ingredient_quantity, quantity);
            Intent detailsIntent = new Intent();
            detailsIntent.putExtra(Utils.RECIPE, recipe);
            rv.setOnClickFillInIntent(R.id.item_ingredient, detailsIntent);

//            rv.setTextColor(R.id.tv_ingredient_name, context.getResources().getColor(android.R.color.black));
//            rv.setTextColor(R.id.tv_ingredient_quantity, context.getResources().getColor(android.R.color.black));
            return rv;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
