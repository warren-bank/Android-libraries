package com.github.warren_bank.filterablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

abstract public class FilterableViewHolder extends RecyclerView.ViewHolder {
    final protected View view;
    final protected List filteredList;

    final private FilterableListItemOnClickListener listener;

    public FilterableViewHolder(
        View view,
        List<FilterableListItem> filteredList,
        FilterableListItemOnClickListener listener
    ) {
        super(view);
        this.view         = view;
        this.filteredList = filteredList;
        this.listener     = listener;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                FilterableListItem item = filteredList.get(position);
                listener.onFilterableListItemClick(item);
            }
        });

        onCreate(view);
    }

    // hook to allow subclass to store private references to UI elements
    abstract public void onCreate(View view);

    // hook to allow subclass to update UI elements when holder is bound to a particular data list item
    abstract public void onUpdate(FilterableListItem item);
}
