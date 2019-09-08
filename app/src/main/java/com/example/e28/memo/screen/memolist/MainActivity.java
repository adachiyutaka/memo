package com.example.e28.memo.screen.memolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.e28.memo.R;
import com.example.e28.memo.model.Memo;
import java.util.ArrayList;
import java.util.List;
;
import io.realm.Realm;
import io.realm.RealmResults;
;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Realm realm;
    TaggedRecyclerViewAdapter adapter;


    //とりあえずのデータセット
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
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recyclerView
        realm = Realm.getDefaultInstance();
        RealmResults<Memo> memoRealmResults = realm.where(Memo.class).findAll();
        adapter = new TaggedRecyclerViewAdapter(memoRealmResults);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_tag);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(llm);

        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.action_reminder) {
            //new ReminderDialogFragment().show(getSupportFragmentManager(), "reminder");
            Intent intent = new Intent(this, com.example.e28.memo.screen.reminder.ReminderDialogActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_highlight) {
            return true;
        }

        if (id == R.id.action_tag) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_drawer) {

        } else if (id == R.id.switch_style_column) {

        } else if (id == R.id.switch_style_sort) {

        } else if (id == R.id.switch_style_tag_group) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, com.example.e28.memo.screen.manage.ManageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_delete) {
            Intent intent = new Intent(this, com.example.e28.memo.screen.WriteActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Activityが破棄される際にrealmのインスタンスを閉じる
        realm.close();
    }
}
