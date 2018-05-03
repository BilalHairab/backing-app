package com.bilal.backing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.interfaces.OnRecipeSelected;
import com.bilal.backing.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder> {

    private List<Recipe> mRecipes;
    private OnRecipeSelected onRecipeSelected;

    public RecipesAdapter(List<Recipe> list, OnRecipeSelected onRecipeSelected) {
        this.mRecipes = list;
        this.onRecipeSelected = onRecipeSelected;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeHolder holder, final int position) {
        holder.tvName.setText(mRecipes.get(holder.getAdapterPosition()).getName());
        String path = mRecipes.get(holder.getAdapterPosition()).getImage();
        if (!path.trim().contentEquals(""))
            Picasso.get().load(path).into(holder.imStep);
        holder.tvServed.setText(String.valueOf(mRecipes.get(holder.getAdapterPosition()).getServings()));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeSelected.onRecipeChoose(holder.getAdapterPosition());
            }
        });
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_served)
        TextView tvServed;

        @BindView(R.id.imageView2)
        ImageView imStep;
        View item;

        RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item = itemView;
        }

    }

}
