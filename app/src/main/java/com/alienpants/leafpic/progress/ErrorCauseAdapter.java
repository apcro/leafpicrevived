package com.alienpants.leafpic.progress;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alienpants.leafpic.R;
import com.alienpants.liz.ThemedAdapter;

import java.util.ArrayList;
import java.util.List;

public class ErrorCauseAdapter extends ThemedAdapter<ErrorCauseViewHolder> {

    private List<ErrorCause> errors;

    public ErrorCauseAdapter(Context context, List<ErrorCause> errors) {
        super(context);
        this.errors = errors;
    }

    public void setErrors(ArrayList<ErrorCause> errors) {
        this.errors = errors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ErrorCauseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ErrorCauseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error_cause, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ErrorCauseViewHolder holder, int position) {
        holder.load(errors.get(position));
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return errors.size();
    }
}
