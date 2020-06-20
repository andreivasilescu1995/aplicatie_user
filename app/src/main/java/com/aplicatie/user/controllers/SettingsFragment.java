package com.aplicatie.user.controllers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.aplicatie.user.MainActivity;
import com.aplicatie.user.R;
import com.aplicatie.user.misc_objects.Constants;

public class SettingsFragment extends Fragment {
    private EditText location_update_interval;
    private CheckBox device_data, mask_card, mask_cvv, vault_payments;
    private ActionBar toolbar;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        location_update_interval = v.findViewById(R.id.edit_update_interval);
        device_data = v.findViewById(R.id.check_device_data);
        mask_card = v.findViewById(R.id.check_mask_card);
        mask_cvv = v.findViewById(R.id.check_mask_cvv);
        vault_payments = v.findViewById(R.id.check_vault_payments);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(getResources().getString(R.string.menu_settings));
        location_update_interval.setText(String.valueOf(getUpdateInterval()));
        location_update_interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!location_update_interval.getText().toString().equals("")) {
                    setUpdateInterval(Integer.parseInt(location_update_interval.getText().toString()));
                }
            }
        });

        device_data.setChecked(getCollectDeviceData());
        device_data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setCollectDeviceData(device_data.isChecked());
            }
        });

        mask_card.setChecked(getMaskCard());
        mask_card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMaskCard(mask_card.isChecked());
            }
        });

        mask_cvv.setChecked(getMaskCvv());
        mask_cvv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMaskCvv(mask_cvv.isChecked());
            }
        });

        vault_payments.setChecked(getVaultPayments());
        vault_payments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVaultPayments(vault_payments.isChecked());
            }
        });
    }

    public static int getUpdateInterval() {
        if (MainActivity.settingsSharedPref.contains("location_update_interval")) {
            return MainActivity.settingsSharedPref.getInt("location_update_interval", 0);
        }
        else {
            setUpdateInterval(Constants.default_update_interval);
            return Constants.default_update_interval;
        }
    }

    private static void setUpdateInterval(int interval) {
        MainActivity.settingsSharedPref.edit().putInt("location_update_interval", interval).apply();
    }

    public static boolean getCollectDeviceData() {
        if (MainActivity.settingsSharedPref.contains("collect_device_data")) {
            return MainActivity.settingsSharedPref.getBoolean("collect_device_data", true);
        }
        else {
            setCollectDeviceData(Constants.collect_device_data);
            return Constants.collect_device_data;
        }
    }

    private static void setCollectDeviceData(Boolean collect) {
        MainActivity.settingsSharedPref.edit().putBoolean("collect_device_data", collect).apply();
    }

    public static boolean getMaskCard() {
        if (MainActivity.settingsSharedPref.contains("mask_card"))
            return MainActivity.settingsSharedPref.getBoolean("mask_card", true);
        else {
            setMaskCard(Constants.mask_card);
            return Constants.mask_card;
        }
    }

    private static void setMaskCard(Boolean mask) {
        MainActivity.settingsSharedPref.edit().putBoolean("mask_card", mask).apply();
    }

    public static boolean getMaskCvv() {
        if (MainActivity.settingsSharedPref.contains("mask_cvv"))
            return MainActivity.settingsSharedPref.getBoolean("mask_cvv", true);
        else {
            setMaskCvv(Constants.mask_code);
            return Constants.mask_card;
        }
    }

    private static void setMaskCvv(Boolean mask) {
        MainActivity.settingsSharedPref.edit().putBoolean("mask_cvv", mask).apply();
    }

    public static boolean getVaultPayments() {
        if (MainActivity.settingsSharedPref.contains("vault_payments"))
            return MainActivity.settingsSharedPref.getBoolean("vault_payments", true);
        else {
            setVaultPayments(Constants.vault_payments);
            return Constants.vault_payments;
        }
    }

    private static void setVaultPayments(Boolean vault) {
        MainActivity.settingsSharedPref.edit().putBoolean("vault_payments", vault).apply();
//        if (!vault)
//            CustomerId.delCustomerId();
    }
}
