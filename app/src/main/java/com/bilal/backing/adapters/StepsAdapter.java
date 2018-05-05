package com.bilal.backing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilal.backing.R;
import com.bilal.backing.interfaces.OnStepSelected;
import com.bilal.backing.models.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bilal on 25/04/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {
    private List<Step> steps;
    private OnStepSelected onStepItemClick;

    public StepsAdapter(List<Step> steps, OnStepSelected onStepItemClick) {
        this.steps = steps;
        this.onStepItemClick = onStepItemClick;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_step, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepHolder holder, final int position) {
        holder.tvStep.setText(steps.get(position).getShortDescription());
        String path = steps.get(position).getThumbnailURL();
        if (!TextUtils.isEmpty(path))
            Picasso.get().load(path).into(holder.imStep);
        holder.tvStep.setText(steps.get(position).getShortDescription());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepItemClick.showStepInfo(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class StepHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step)
        TextView tvStep;

        @BindView(R.id.im_step)
        ImageView imStep;
        View item;

        StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item = itemView;
        }

    }

}
