package com.bilal.backing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends BaseAdapter {

    private List<Recipe> mRecipes;
    private Context context;

    public RecipesAdapter(Context context, List<Recipe> list) {
        this.context = context;
        this.mRecipes = list;
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        if (position >= mRecipes.size())
            return null;
        else
            return mRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item;
        if (convertView == null) {

            item = inflater.inflate(R.layout.item_recipe, parent, false);
            TextView name = item.findViewById(R.id.tv_name);
            TextView served = item.findViewById(R.id.tv_served);
            ImageView recipePhoto = item.findViewById(R.id.im_recipe);
            name.setText(mRecipes.get(position).getName());
            served.setText(String.valueOf(mRecipes.get(position).getServings()));
            String path = mRecipes.get(position).getImage();
            if (!path.trim().isEmpty())
                Picasso.get().load(path).into(recipePhoto);
        } else {
            item = convertView;
        }
        return item;
    }
}
