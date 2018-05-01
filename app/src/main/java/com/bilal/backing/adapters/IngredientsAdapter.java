package com.bilal.backing.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.models.Ingredient;

import java.util.HashMap;
import java.util.List;

public class IngredientsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listHeaders;
    private HashMap<String, List<Ingredient>> listChildren;

    public IngredientsAdapter(Context context, List<String> listDataHeader, HashMap<String, List<Ingredient>> listChildData) {
        this.context = context;
        this.listHeaders = listDataHeader;
        this.listChildren = listChildData;
    }

    @Override
    public Ingredient getChild(int groupPosition, int childPosititon) {
        return this.listChildren.get(this.listHeaders.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Ingredient ingredient = getChild(groupPosition, childPosition);

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

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChildren.get(this.listHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.group_ingredient, parent, false);
        }
        TextView listHeader = convertView.findViewById(R.id.tv_gp_ingredients);
        listHeader.setTypeface(null, Typeface.BOLD);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

