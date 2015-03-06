package com.gunner.section.recyclerview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gunner.sectionrecyclerview.BaseSectionRecyclerViewAdapter;
import com.gunner.sectionrecyclerview.SectionRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private SectionRecyclerView sectionRecyclerView;

    private CategoryGoodsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sectionRecyclerView = (SectionRecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CategoryGoodsAdapter();
        mAdapter.setHasHeader(true);
        mAdapter.setHasFooter(true);
        sectionRecyclerView.setAdapter(mAdapter);
        mAdapter.refreshViewByData(parseMockData());

        mAdapter.setOnItemClickListener(new BaseSectionRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClickListener(View view, int groupPosition) {
                Toast.makeText(getApplicationContext(), "Group position " + groupPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildItemClickListener(View view, int groupPosition, int childPosition) {
                Toast.makeText(getApplicationContext(), "Group position " + groupPosition + ",Child position " + childPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Category> parseMockData() {
        List<Category> categoryList;
        String jsonData = getResources().getString(R.string.data);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            categoryList = Category.mockData(jsonObject.optString("categories"));
        } catch (JSONException e) {
            categoryList = new ArrayList<>();
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_grid) {
            sectionRecyclerView.setLayoutType(SectionRecyclerView.LayoutType.Grid);
            mAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.action_list) {
            sectionRecyclerView.setLayoutType(SectionRecyclerView.LayoutType.List);
            mAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
