package com.example.user_vs.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private EditText fullName, country, university, age;
    TextView languages;
    Button languageButton;
    String[] listLanguages;
    boolean[] cheakedItems;
    ArrayList<Integer> selectedLanguages = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = view.findViewById(R.id.profile_fullName);
        country = view.findViewById(R.id.profile_country);
        university = view.findViewById(R.id.profile_university);
        age = view.findViewById(R.id.profile_age);
        languageButton = view.findViewById(R.id.profile_button_lang);
        languages = view.findViewById(R.id.profile_list_lang);
        listLanguages = getResources().getStringArray(R.array.list_language);
        cheakedItems = new boolean[listLanguages.length];

        languageButton.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle(R.string.dialog_title);
            mBuilder.setMultiChoiceItems(listLanguages, cheakedItems, (dialog, which, isChecked) -> {
                if (isChecked) {
                    selectedLanguages.add(which);
                } else {
                    selectedLanguages.remove((Integer.valueOf(which)));
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton(getString(R.string.ok_label), (dialog, which) -> {
                StringBuilder lang = new StringBuilder();
                for (int i = 0; i < selectedLanguages.size(); i++) {
                    lang.append(listLanguages[selectedLanguages.get(i)]);
                    if (i != selectedLanguages.size() - 1) {
                        lang.append(", ");
                    }
                }
                languages.setText(lang.toString());
            });

            mBuilder.setNegativeButton(R.string.dismiss_label, (dialog, which) -> dialog.dismiss());

            mBuilder.setNeutralButton(R.string.clear_all_label, (dialog, which) -> {
                for (int i = 0; i < cheakedItems.length; i++) {
                    cheakedItems[i] = false;
                    selectedLanguages.clear();
                    languages.setText("");
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
    }
}
