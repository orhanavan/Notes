package com.peanutbutterapps.notes.viewFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.peanutbutterapps.notes.R;

public class PaletteFragment extends AppCompatDialogFragment implements View.OnClickListener {

    private View view;
    private ColorDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ThemeOverlay_AppCompat_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.palette_layout, null);
        builder.setView(view);
        initButtons();
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ColorDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ColorDialogListener");
        }
    }

    @Override
    public void onClick(View v) {

        int color = 0;

        switch (v.getId()) {
            case R.id.palette_red:
                color = R.color.red;
                break;
            case R.id.palette_pink:
                color = R.color.pink;
                break;
            case R.id.palette_purple:
                color = R.color.purple;
                break;
            case R.id.palette_indigo:
                color = R.color.indigo;
                break;
            case R.id.palette_blue:
                color = R.color.blue;
                break;
            case R.id.palette_white:
                color = android.R.color.white;
                break;
            case R.id.palette_yellow:
                color = R.color.yellow;
                break;
            case R.id.palette_orange:
                color = R.color.orange;
                break;
            case R.id.palette_green:
                color = R.color.green;
                break;
            case R.id.palette_teal:
                color = R.color.teal;
                break;
        }

        listener.applyColor(color);
        dismiss();
    }

    private void initButtons() {
        ImageView red = view.findViewById(R.id.palette_red);
        ImageView pink = view.findViewById(R.id.palette_pink);
        ImageView purple = view.findViewById(R.id.palette_purple);
        ImageView indigo = view.findViewById(R.id.palette_indigo);
        ImageView blue = view.findViewById(R.id.palette_blue);
        ImageView white = view.findViewById(R.id.palette_white);
        ImageView yellow = view.findViewById(R.id.palette_yellow);
        ImageView orange = view.findViewById(R.id.palette_orange);
        ImageView green = view.findViewById(R.id.palette_green);
        ImageView teal = view.findViewById(R.id.palette_teal);

        red.setOnClickListener(this);
        pink.setOnClickListener(this);
        purple.setOnClickListener(this);
        indigo.setOnClickListener(this);
        blue.setOnClickListener(this);
        white.setOnClickListener(this);
        yellow.setOnClickListener(this);
        orange.setOnClickListener(this);
        green.setOnClickListener(this);
        teal.setOnClickListener(this);

    }

    public interface ColorDialogListener {
        void applyColor(int color);
    }

}
