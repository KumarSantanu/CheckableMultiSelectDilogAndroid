package com.fragmentdilog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MultiLineAdpter extends RecyclerView.Adapter< MultiLineAdpter.MultiSelectViewHolder > {

    private ArrayList< MultilineModel > mDataSet = new ArrayList<>();
    private String mSearchQuery = "";
    private final Context mContext;

    public MultiLineAdpter(ArrayList< MultilineModel > mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MultiSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multiselect, parent, false);
        return new MultiSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiSelectViewHolder holder, int position) {

        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(position, holder.dialog_name_item);
        } else {
            holder.dialog_name_item.setText(mDataSet.get(position).getDisplayName());
        }

        if (mDataSet.get(position).getSelected()) {

            if (!MultiSelectedDilog.selectedIdsForCallback.contains(mDataSet.get(position).getId())) {
                MultiSelectedDilog.selectedIdsForCallback.add(mDataSet.get(position).getId());
            }
        }

        if (checkForSelection(mDataSet.get(position).getId())) {
            holder.dialog_item_checkbox.setChecked(true);
        } else {
            holder.dialog_item_checkbox.setChecked(false);
        }

        holder.main_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectedDilog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    removeFromSelection((mDataSet.get(holder.getAdapterPosition()).getId()));
                    holder.dialog_item_checkbox.setChecked(false);
                    mDataSet.get(holder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

    }

    private void setHighlightedText(int position, TextView textview) {
        String name = mDataSet.get(position).getDisplayName();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{ContextCompat.getColor(mContext, R.color.purple_500)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectedDilog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectedDilog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < MultiSelectedDilog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectedDilog.selectedIdsForCallback.get(i))) {
                MultiSelectedDilog.selectedIdsForCallback.remove(i);
            }
        }
    }


    void setData(ArrayList< MultilineModel > data, String query, MultiLineAdpter mutliSelectAdapter) {
        this.mDataSet = data;
        this.mSearchQuery = query;
        mutliSelectAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class MultiSelectViewHolder extends RecyclerView.ViewHolder {

        private TextView dialog_name_item;
        private AppCompatCheckBox dialog_item_checkbox;
        private LinearLayout main_container;

        public MultiSelectViewHolder(@NonNull View view) {
            super(view);
            dialog_name_item = (TextView) view.findViewById(R.id.dialog_item_name);
            dialog_item_checkbox = (AppCompatCheckBox) view.findViewById(R.id.dialog_item_checkbox);
            main_container = (LinearLayout) view.findViewById(R.id.main_container);
        }
    }
}
