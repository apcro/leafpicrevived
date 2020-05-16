package com.alienpants.leafpicrevived.progress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alienpants.leafpicrevived.R;

import org.horaapps.liz.ThemedAdapter;

import java.util.ArrayList;
import java.util.List;

public class ErrorCauseAdapter extends ThemedAdapter<ErrorCauseViewHolder> {

    private List<ErrorCause> errors;

    ErrorCauseAdapter(Context context, List<ErrorCause> errors) {
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
