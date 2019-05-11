package com.github.warren_bank.filterablerecyclerview;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class FilterableAdapter extends RecyclerView.Adapter<FilterableViewHolder> implements Filterable {
    final private int row_layout_id;
    final private ArrayList<FilterableListItem> unfilteredList;
    final private ArrayList<FilterableListItem> filteredList;
    final private FilterableListItemOnClickListener listener;
    final private Class filterableViewHolderClass;
    final private Class parentClass;
    final private Object parentInstance;

    public FilterableAdapter(
        int row_layout_id,
        List<FilterableListItem> unfilteredList,
        FilterableListItemOnClickListener listener,
        Class filterableViewHolderClass
    ) {
        this(
            row_layout_id,
            unfilteredList,
            listener,
            filterableViewHolderClass,
            (Class) null,
            (Object) null
        );
    }

    public FilterableAdapter(
        int row_layout_id,
        List<FilterableListItem> unfilteredList,
        FilterableListItemOnClickListener listener,
        Class filterableViewHolderClass,
        Class parentClass,
        Object parentInstance
    ) {
        this.row_layout_id             = row_layout_id;
        this.unfilteredList            = new ArrayList<FilterableListItem>(unfilteredList);
        this.filteredList              = new ArrayList<FilterableListItem>();
        this.listener                  = listener;
        this.filterableViewHolderClass = filterableViewHolderClass;
        this.parentClass               = parentClass;
        this.parentInstance            = parentInstance;

        resetFilteredList();
    }

    private void resetFilteredList() {
        filteredList.clear();
        filteredList.addAll(
            unfilteredList
        );
    }

    @Override
    public FilterableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(row_layout_id, parent, false);
            Class[] cArg;
            FilterableViewHolder holder;

            if ((parentClass == null) || (parentInstance == null)) {
                cArg     = new Class[3];
                cArg[0]  = View.class;
                cArg[1]  = List.class;
                cArg[2]  = FilterableListItemOnClickListener.class;

                holder = (FilterableViewHolder) filterableViewHolderClass.getDeclaredConstructor(cArg).newInstance(
                    view,
                    filteredList,
                    listener
                );
            }
            else {
                cArg     = new Class[4];
                cArg[0]  = parentClass;
                cArg[1]  = View.class;
                cArg[2]  = List.class;
                cArg[3]  = FilterableListItemOnClickListener.class;

                holder = (FilterableViewHolder) filterableViewHolderClass.getDeclaredConstructor(cArg).newInstance(
                    parentInstance,
                    view,
                    filteredList,
                    listener
                );
            }

            return holder;
        }
        catch(Exception e) {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(FilterableViewHolder holder, final int position) {
        try {
            final FilterableListItem item = filteredList.get(position);

            holder.onUpdate(item);
        }
        catch(Exception e) {}
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    resetFilteredList();
                } else {
                    filteredList.clear();
                    for (FilterableListItem item : unfilteredList) {
                        String filterableValue = item.getFilterableValue();

                        if (filterableValue.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }
}
