package com.peanutbutterapps.notes.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.peanutbutterapps.notes.R;
import com.peanutbutterapps.notes.model.Note;
import com.peanutbutterapps.notes.viewFragment.HelpFragment;
import com.peanutbutterapps.notes.viewFragment.NoteFragment;
import com.peanutbutterapps.notes.viewModel.NoteViewModel;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int COLUMN_NUM_PORTRAIT = 2;
    public static int COLUMN_NUM_LANDSCAPE = 3;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static NoteViewModel noteViewModel;
    private MenuItem add_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        getScreenSize();


        // start note fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NoteFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_notes);
        }

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
    }

    private void initLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditActivity.EXTRA_DESC);
            int color = data.getIntExtra(AddEditActivity.EXTRA_COLOR, android.R.color.white);
            Date date = (Date) data.getSerializableExtra(AddEditActivity.EXTRA_DATE);

            Note note = new Note(title, desc, color, date);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getApplicationContext(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditActivity.EXTRA_DESC);
            int color = data.getIntExtra(AddEditActivity.EXTRA_COLOR, android.R.color.white);
            Date date = (Date) data.getSerializableExtra(AddEditActivity.EXTRA_DATE);

            Note note = new Note(title, desc, color, date);
            note.setKey(id);
            noteViewModel.update(note);

            Toast.makeText(getApplicationContext(), "Note updated.", Toast.LENGTH_SHORT).show();

        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        add_note = menu.getItem(0);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_notes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NoteFragment()).commit();
                add_note.setVisible(true);
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpFragment()).commit();
                add_note.setVisible(false);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {

            COLUMN_NUM_PORTRAIT = 4;
            COLUMN_NUM_LANDSCAPE = 5;

        } else if (smallestWidth > 600) {

            COLUMN_NUM_PORTRAIT = 3;
            COLUMN_NUM_LANDSCAPE = 4;

        } else {
            COLUMN_NUM_PORTRAIT = 2;
            COLUMN_NUM_LANDSCAPE = 3;
        }
    }
}
