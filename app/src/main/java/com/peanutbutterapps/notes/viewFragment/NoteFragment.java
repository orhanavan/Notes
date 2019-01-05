package com.peanutbutterapps.notes.viewFragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.peanutbutterapps.notes.R;
import com.peanutbutterapps.notes.adapter.NoteAdapter;
import com.peanutbutterapps.notes.model.Note;
import com.peanutbutterapps.notes.view.AddEditActivity;
import com.peanutbutterapps.notes.view.MainActivity;
import com.peanutbutterapps.notes.viewModel.NoteViewModel;

import java.util.List;


public class NoteFragment extends Fragment {


    public NoteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        getActivity().setTitle(R.string.app_name);

        // init recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(
                    new StaggeredGridLayoutManager(MainActivity.COLUMN_NUM_PORTRAIT, LinearLayout.VERTICAL));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(
                    new StaggeredGridLayoutManager(MainActivity.COLUMN_NUM_LANDSCAPE, LinearLayout.VERTICAL));

        }
        recyclerView.setHasFixedSize(true);

        // init adapter
        final NoteAdapter adapter = new NoteAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        // init viewModel

        MainActivity.noteViewModel.getAllNotes().observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        // note swipe animation
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                MainActivity.noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        // item click
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note, View view) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);

                intent.putExtra(AddEditActivity.EXTRA_ID, note.getKey());
                intent.putExtra(AddEditActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditActivity.EXTRA_DESC, note.getDescription());
                intent.putExtra(AddEditActivity.EXTRA_COLOR, note.getColor());
                intent.putExtra(AddEditActivity.EXTRA_DATE, note.getCreteEditDate());

                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
                getActivity().startActivityForResult(intent, MainActivity.EDIT_NOTE_REQUEST, optionsCompat.toBundle());
            }
        });

//        // item long click
//        adapter.setOnItemLongClickListener(new NoteAdapter.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(Note note) {
//                if (actionMode == null) {
//                    actionMode = startSupportActionMode(actionModeCallback);
//                }
//            }
//        });

        return view;
    }

}
