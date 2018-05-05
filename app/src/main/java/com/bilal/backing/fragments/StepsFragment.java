package com.bilal.backing.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bilal.backing.R;
import com.bilal.backing.utils.Utils;
import com.bilal.backing.activities.StepDetailActivity;
import com.bilal.backing.adapters.StepsAdapter;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.interfaces.OnStepSelected;
import com.bilal.backing.models.Recipe;
import com.bilal.backing.views.MyRecyclerItemDecoration;
import com.bilal.backing.views.SimpleCustomPop;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment implements OnStepSelected {
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_TABLET = "tablet";

    @BindView(R.id.btn_ingredients)
    Button btnIngredients;

    @BindView(R.id.rv_steps)
    RecyclerView recyclerView;

    Recipe mRecipe;
    boolean isTablet;
    OnStepChanged onStepChanged;
    static final String STEPS_STATE = "steps_state";
    static final String LIST_STATE = "list";
    Parcelable stepsState;
    LinearLayoutManager linearLayoutManager;
    Context context;
    SimpleCustomPop mQuickCustomPopup;

    public StepsFragment() {

    }

    public static StepsFragment newInstance(Recipe recipe, boolean isTablet) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putBoolean(ARG_TABLET, isTablet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(ARG_RECIPE);
            isTablet = savedInstanceState.getBoolean(ARG_TABLET);
            stepsState = savedInstanceState.getParcelable(STEPS_STATE);
        } else if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(ARG_RECIPE);
            isTablet = getArguments().getBoolean(ARG_TABLET);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        mQuickCustomPopup = new SimpleCustomPop(context, mRecipe.getIngredients());
        if (savedInstanceState != null && savedInstanceState.getBoolean(LIST_STATE) && !mQuickCustomPopup.isShowing())
            showIngredientsList();
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ButterKnife.bind(this, view);
        if (mRecipe != null) {
            adapt();
        } else getActivity().finish();
        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuickCustomPopup != null) {
                    showIngredientsList();
                }
            }
        });
    }

    @Override
    public void showStepInfo(int position) {
        if (isTablet) {
            onStepChanged.onChangeStep(position);
        } else {
            Intent intent = new Intent(context, StepDetailActivity.class);
            intent.putExtra(ARG_RECIPE, mRecipe);
            intent.putExtra(Utils.STEP_SEQUENCE, position);
            context.startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepChanged = (OnStepChanged) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepChanged ");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (linearLayoutManager != null)
            outState.putParcelable(STEPS_STATE, linearLayoutManager.onSaveInstanceState());
        if (mQuickCustomPopup != null && mQuickCustomPopup.isShowing())
            outState.putBoolean(LIST_STATE, true);
        outState.putParcelable(ARG_RECIPE, mRecipe);
        outState.putBoolean(ARG_TABLET, isTablet);
        super.onSaveInstanceState(outState);
        Log.e("bilal_onSaveFragment", "on");
    }

    void showIngredientsList() {
        mQuickCustomPopup
                .anchorView(btnIngredients)
                .offset(10, -5)
                .gravity(Gravity.BOTTOM)
                .showAnim(new BounceBottomEnter())
                .dismissAnim(new SlideBottomExit())
                .dimEnabled(true)
                .show();
    }

    void adapt() {
        MyRecyclerItemDecoration itemDecoration = new MyRecyclerItemDecoration(9, 4);
        recyclerView.removeItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        StepsAdapter stepsAdapter = new StepsAdapter(mRecipe.getSteps(), this);
        recyclerView.setAdapter(stepsAdapter);
        if (stepsState != null)
            linearLayoutManager.onRestoreInstanceState(stepsState);
    }

}
