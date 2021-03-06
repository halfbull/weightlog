package com.github.halfbull.weightlog.weightlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.halfbull.weightlog.R;
import com.github.halfbull.weightlog.database.Weight;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

class WeightLogAdapter extends RecyclerView.Adapter<WeightLogAdapter.WeightViewHolder> {

    private Context context;
    private WeightDiffList weightDiffs;

    private int selectedItemPosition;

    WeightLogAdapter(Context context) {
        weightDiffs = new WeightDiffList(new LinkedList<Weight>());
        this.context = context;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_log_row, parent, false);

        return new WeightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightDiff weightDiff = weightDiffs.getDiff(position);
        holder.bind(weightDiff);
    }

    @Override
    public int getItemCount() {
        return weightDiffs.size();
    }

    void setModel(WeightDiffList weightDiffs) {
        this.weightDiffs = weightDiffs;
        notifyDataSetChanged();
    }

    private void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
    }

    int getSelectedItemPosition(){
        return selectedItemPosition;
    }

    class WeightViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        @SuppressWarnings("SpellCheckingInspection")
        final SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        @SuppressWarnings("SpellCheckingInspection")
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, kk:mm", Locale.getDefault());

        private final TextView diff;
        private final TextView value;
        private final TextView dayOfWeek;
        private final TextView date;

        WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);

            diff = itemView.findViewById(R.id.diff_tv);
            value = itemView.findViewById(R.id.value_tv);
            dayOfWeek = itemView.findViewById(R.id.day_of_week);
            date = itemView.findViewById(R.id.date_tv);
        }

        void bind(@NonNull WeightDiff weightDiff) {
            diff.setText(formatDiff(weightDiff.getDiff()));
            value.setText(formatFloat(weightDiff.getValue()));
            dayOfWeek.setText(dayOfWeekFormat.format(weightDiff.getDate()));
            date.setText(formatDate(weightDiff.getDate()));

            if (weightDiff.getDiff() >= 0) {
                diff.setTextColor(context.getResources().getColor(R.color.positive_weight_diff, context.getTheme()));
            } else {
                diff.setTextColor(context.getResources().getColor(R.color.negative_weight_diff, context.getTheme()));
            }
        }

        private String formatFloat(float v) {
            return String.format(Locale.US, "%.1f", v);
        }

        private String formatDiff(float v) {
            String str = formatFloat(v);
            if (v == 0)
                return "";
            if (v > 0)
                return "+" + str;

            return str;
        }

        private String formatDate(Date date) {
            return dateFormat.format(date);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            WeightLogAdapter.this.setSelectedItemPosition(getAdapterPosition());
            contextMenu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.weight_log_context_menu_delete);
        }
    }
}
