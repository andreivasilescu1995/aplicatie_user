package com.aplicatie.user.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.aplicatie.user.R;

public class ErrorDialog extends DialogFragment {
    private TextView title;
    private TextView message;

    public ErrorDialog() {}

    public static ErrorDialog newInstance(String title, String message) {
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_error, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title_error);
        message = view.findViewById(R.id.message_error);
        String title = getArguments().getString("title", "");
        String message = getArguments().getString("message", "");
        this.title.setText(title);
        this.message.setText(message);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, "Error");
    }
}
