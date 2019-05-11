package com.github.warren_bank.filterablerecyclerview.sample;

import com.github.warren_bank.filterablerecyclerview.FilterableListItem;
import com.github.warren_bank.filterablerecyclerview.FilterableListItemOnClickListener;
import com.github.warren_bank.filterablerecyclerview.FilterableViewHolder;
import com.github.warren_bank.filterablerecyclerview.FilterableAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

// -----------------------------------------------------------------------------

class SampleFilterableListItem implements FilterableListItem {
    public String fname;
    public String lname;

    public SampleFilterableListItem(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public String getFilterableValue() {
        return fname + " " + lname;
    }
}

// -----------------------------------------------------------------------------

class SampleFilterableListItemOnClickListener implements FilterableListItemOnClickListener {
    private Context context;

    public SampleFilterableListItemOnClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onFilterableListItemClick(FilterableListItem item) {
        Toast.makeText(context, item.getFilterableValue(), Toast.LENGTH_SHORT).show();
    }
}

// -----------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {
    private List<FilterableListItem>                   unfilteredList;
    private SampleFilterableListItemOnClickListener    recyclerListener;
    private FilterableAdapter                          recyclerFilterableAdapter;
    private RecyclerView                               recyclerView;

    private Filter                                     searchFilter;
    private SearchView                                 searchView;

    protected boolean                                  parentVariable;

    // -------------------------------------------------------------------------

    public class SampleFilterableViewHolder extends FilterableViewHolder {
        private TextView text1;
        private TextView text2;

        public SampleFilterableViewHolder(
            View view,
            List<FilterableListItem> filteredList,
            FilterableListItemOnClickListener listener
        ) {
            super(view, filteredList, listener);
        }

        @Override
        public void onCreate(View view) {
            text1 = view.findViewById(android.R.id.text1);
            text2 = view.findViewById(android.R.id.text2);
        }

        @Override
        public void onUpdate(FilterableListItem filterableListItem) {
            SampleFilterableListItem item = (SampleFilterableListItem) filterableListItem;

            if (parentVariable) {
                text1.setText(item.lname.toLowerCase());
                text2.setText(item.fname.toLowerCase());
            }
            else {
                text1.setText(item.lname.toUpperCase());
                text2.setText(item.fname.toUpperCase());
            }

            MainActivity.this.parentVariable = (!parentVariable);
        }
    }

    // -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.app_name);
        toolbar.setNavigationIcon(null);

        unfilteredList             = Data.getSampleList();
        recyclerListener           = new SampleFilterableListItemOnClickListener(this);
        recyclerFilterableAdapter  = new FilterableAdapter(
            android.R.layout.two_line_list_item,       // https://github.com/aosp-mirror/platform_frameworks_base/blob/master/core/res/res/layout/two_line_list_item.xml
            unfilteredList,
            recyclerListener,
            SampleFilterableViewHolder.class,
            MainActivity.class,
            MainActivity.this
        );

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerFilterableAdapter);

        // add divider between list items
        recyclerView.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );

        searchFilter = recyclerFilterableAdapter.getFilter();

        parentVariable = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchFilter.filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
