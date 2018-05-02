package com.bilal.backing.views;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bilal.backing.R;
import com.bilal.backing.adapters.IngredientsAdapter;
import com.bilal.backing.models.Ingredient;
import com.flyco.dialog.widget.popup.base.BasePopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleCustomPop extends BasePopup<SimpleCustomPop> {

    @BindView(R.id.lv_ingredients)
    ListView listView;
    private Context context;
    private List<Ingredient> ingredients;

    public SimpleCustomPop(Context context, List<Ingredient> ingredients) {
        super(context);
        this.context = context;
        this.ingredients = ingredients;
        setCanceledOnTouchOutside(true);
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(mContext, R.layout.popup_ingredients, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        ArrayAdapter<Ingredient> adapter = new IngredientsAdapter(context, ingredients);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
            }
        });
    }
}