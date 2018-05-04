package com.bilal.backing.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.bilal.backing.adapters.IngredientsViewFactory;

public class IngredientWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsViewFactory(this.getApplicationContext());
    }
}
