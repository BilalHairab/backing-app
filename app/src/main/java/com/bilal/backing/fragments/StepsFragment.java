package com.bilal.backing.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.bilal.backing.R;
import com.bilal.backing.Utils;
import com.bilal.backing.activities.StepDetailActivity;
import com.bilal.backing.adapters.IngredientsAdapter;
import com.bilal.backing.adapters.StepsAdapter;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.interfaces.OnStepSelected;
import com.bilal.backing.models.Ingredient;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.views.MyRecyclerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment implements OnStepSelected {
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_TABLET = "tablet";
    private static final String ARG_STEP_CHANGED = "changed";
    @BindView(R.id.elv_ingredients)
    ExpandableListView listViewIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView recyclerView;

    Recipe mRecipe;
    boolean isTablet;
    OnStepChanged onStepChanged;
    HashMap<String, List<Ingredient>> ingredientsMap = new HashMap<>();

    List<String> header = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    Context context;

    public StepsFragment() {

    }

    public static StepsFragment newInstance(Recipe recipe, boolean isTablet, OnStepChanged onStepChanged) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putBoolean(ARG_TABLET, isTablet);
        args.putSerializable(ARG_STEP_CHANGED, onStepChanged);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
            isTablet = getArguments().getBoolean(ARG_TABLET);
            onStepChanged = (OnStepChanged) getArguments().getSerializable(ARG_STEP_CHANGED);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.rv_steps);
        recyclerView.setHasFixedSize(true);
        if (mRecipe != null) {
            header.add("Ingredients");
            ingredientsMap.put(header.get(0), mRecipe.getIngredients());
            IngredientsAdapter adapter = new IngredientsAdapter(context, header, ingredientsMap);
            listViewIngredients.setAdapter(adapter);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            MyRecyclerItemDecoration itemDecoration = new MyRecyclerItemDecoration(9, 4);
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            StepsAdapter stepsAdapter = new StepsAdapter(mRecipe.getSteps(), this);
            recyclerView.setAdapter(stepsAdapter);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showStepInfo(int position) {
        if (isTablet) {
            onStepChanged.onChangeStep(position);
        } else {
            Intent intent = new Intent(context, StepDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(ARG_RECIPE, mRecipe);
            intent.putExtra(Utils.STEP_SEQUENCE, position);
            context.startActivity(intent);
            getActivity().finish();
        }
    }
}
