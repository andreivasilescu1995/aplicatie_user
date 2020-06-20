package com.aplicatie.user.controllers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aplicatie.user.R;

public class ErrorFragment extends DialogFragment {
    private final String message;
    private final String title;
    private TextView error_title;
    private TextView error_message;

    public ErrorFragment(String title, String message) {
        super();
        this.title = title;
        this.message = message;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_error, null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        error_title = requireDialog().findViewById(R.id.title_error);
        error_message = requireDialog().findViewById(R.id.message_error);
        error_title.setText(title);
        error_message.setText(message);
        requireDialog().getWindow().setLayout(getResources().getDisplayMetrics().widthPixels/2, getResources().getDisplayMetrics().widthPixels/3);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (isAdded() || isVisible() || isInLayout())
            Log.e("TEST", "DEJA VIZIBIL");
        super.show(manager, tag);
    }
}
