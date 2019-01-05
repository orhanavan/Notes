package com.peanutbutterapps.notes.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.peanutbutterapps.notes.R;
import com.peanutbutterapps.notes.viewFragment.PaletteFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEditActivity extends AppCompatActivity implements PaletteFragment.ColorDialogListener {

    public static final String EXTRA_ID = "com.peanutbutterapps.notes.view.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.peanutbutterapps.notes.view.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.peanutbutterapps.notes.view.EXTRA_DESC";
    public static final String EXTRA_COLOR = "com.peanutbutterapps.notes.view.EXTRA_COLOR";
    public static final String EXTRA_DATE = "com.peanutbutterapps.notes.view.EXTRA_DATE";

    private EditText editTextTitle;
    private EditText editTextDesc;
    private TextView textViewDate;
    private RelativeLayout relativeLayout;
    private int localColor = android.R.color.white;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setElevation(0);

        editTextTitle = findViewById(R.id.add_edit_title);
        editTextDesc = findViewById(R.id.add_edit_description);
        textViewDate = findViewById(R.id.add_edit_date);
        relativeLayout = findViewById(R.id.add_edit_layout);

        // change toolbar title which intent you came from
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");

            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDesc.setText(intent.getStringExtra(EXTRA_DESC));
            Date date = (Date) intent.getSerializableExtra(EXTRA_DATE);
            textViewDate.setText("Last edit: " + getStringDate(date));
            int color = intent.getIntExtra(EXTRA_COLOR, localColor);
            applyColor(color);

        } else {
            setTitle("Add Note");
            applyColor(localColor);

        }

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String desc = editTextDesc.getText().toString();
        if (title.equals("") && desc.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in the blanks.", Toast.LENGTH_SHORT).show();
            return;
        }

        int color = localColor;
        Date date = Calendar.getInstance().getTime();

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, desc);
        data.putExtra(EXTRA_COLOR, color);
        data.putExtra(EXTRA_DATE, date);

        // Save for editing and creating
        // If it has id, make edit.
        // If doesn't have id, create
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }


    private String getStringDate(Date date) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(date)
                + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_palette:
                openPalette();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPalette() {
        new PaletteFragment().show(getSupportFragmentManager(), "palette-fragment");
    }

    @Override
    public void applyColor(int colorCode) {
//        Toast.makeText(getApplicationContext(), ""+colorCode, Toast.LENGTH_SHORT).show();

        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(colorCode));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//            if (colorCode == android.R.color.white || colorCode == R.color.yellow) {
//                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//
//            } else {
//                window.setStatusBarColor(getResources().getColor(colorCode));
//            }
//
//        }

        relativeLayout.setBackgroundColor(getResources().getColor(colorCode));
        localColor = colorCode;


        // Cancelling Fade animation
        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
    }
}
