package com.example.e28.memo.screen;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.e28.memo.R;
import com.example.e28.memo.screen.memolist.TagItem;
import com.example.e28.memo.screen.memolist.TaggedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //recyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_tag);
        TaggedRecyclerViewAdapter adapter = new TaggedRecyclerViewAdapter(this.createDataset());

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);
    }

    private List<TagItem> createDataset() {

        List<TagItem> dataset = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TagItem data = new TagItem();
            data.setTagName("タグ" + i);
            data.setTagSummary("メモ" + i);

            dataset.add(data);
        }
        return dataset;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_trash) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
