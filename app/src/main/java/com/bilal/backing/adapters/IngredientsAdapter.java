package com.bilal.backing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.models.Ingredient;

import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> listChildren;


    public IngredientsAdapter(Context context, List<Ingredient> listChildData) {
        super(context, R.layout.item_ingredient);
        this.context = context;
        this.listChildren = listChildData;
    }

    @Override
    public int getCount() {
        return listChildren.size();
    }

    @Override
    public Ingredient getItem(int position) {
        return listChildren.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Ingredient ingredient = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_ingredient, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.tv_ingredient_name);
        TextView tvQuantity = convertView.findViewById(R.id.tv_ingredient_quantity);
        tvName.setText(ingredient.getIngredient());
        String quantity = ingredient.getQuantity() + " " + ingredient.getMeasure();
        tvQuantity.setText(quantity);
        return convertView;
    }

}

