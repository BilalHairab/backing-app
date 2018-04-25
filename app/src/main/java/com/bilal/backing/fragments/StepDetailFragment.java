package com.bilal.backing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.interfaces.OnStepChanged;
import com.bilal.backing.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {
    private static final String ARG_STEP = "step";
    private static final String ARG_LAST_STEP = "last";
    private static final String ARG_STEP_CHANGED = "changed";
    private OnStepChanged onStepChanged;
    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    private Step step;
    private boolean isLastStep;

    public StepDetailFragment() {

    }

    public static StepDetailFragment newInstance(Step step, boolean isLastStep, OnStepChanged onStepChanged) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);
        args.putBoolean(ARG_LAST_STEP, isLastStep);
        args.putSerializable(ARG_STEP_CHANGED, onStepChanged);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(ARG_STEP);
            isLastStep = getArguments().getBoolean(ARG_LAST_STEP);
            onStepChanged = (OnStepChanged) getArguments().getSerializable(ARG_STEP_CHANGED);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        button.setText(step.getShortDescription());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepChanged.onNextStep();
            }
        });
        if (isLastStep)
            button.setVisibility(View.GONE);
        super.onViewCreated(view, savedInstanceState);
    }
}
