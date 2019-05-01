package com.example.user_vs.fragments;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText fullName, country, university, age, mail;

    private RadioGroup radioSexGroup;
    String gender;

    Button saveButton;

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("profiles")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> map = documentSnapshot.getData();
                        if (map.containsKey("age"))
                            age.setText(map.get("age").toString());
                        if (map.containsKey("country"))
                            country.setText(map.get("country").toString());
                        if (map.containsKey("fullName"))
                            fullName.setText(map.get("fullName").toString());
                        if (map.containsKey("university"))
                            university.setText(map.get("university").toString());
                        if (map.containsKey("gender"))
                            gender = map.get("gender").toString();
                        if (map.containsKey("languages"))
                            languages.setText(map.get("languages").toString());
                        switch (gender) {
                            case "male":
                                radioSexGroup.check(R.id.radioMale);
                                break;
                            case "female":
                                radioSexGroup.check(R.id.radioFemale);
                                break;
                        }
                    }
                });

        fullName = view.findViewById(R.id.profile_fullName);
        country = view.findViewById(R.id.profile_country);
        university = view.findViewById(R.id.profile_university);
        age = view.findViewById(R.id.profile_age);

        mail = view.findViewById(R.id.profile_mail);
        mail.setText(user.getEmail());

        saveButton = view.findViewById(R.id.profile_saveButton);

        radioSexGroup = view.findViewById(R.id.profile_genderGroup);
        radioSexGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioMale:
                    gender = "male";
                    break;
                case R.id.radioFemale:
                    gender = "female";
                    break;
            }
        });


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

        saveButton.setOnClickListener(v -> {
            final String userId = user.getUid();
            final String fullNameStr = fullName.getText().toString();
            final String mailStr = mail.getText().toString();
            final String countryStr = country.getText().toString();
            final String universityStr = university.getText().toString();
            final String ageStr = age.getText().toString();
            String languagesStr = languages.getText().toString();


            if (fullNameStr.isEmpty() || mailStr.isEmpty() || countryStr.isEmpty() || universityStr.isEmpty() || ageStr.isEmpty()) {
                showMessage("Please, verify all fields correctly");
            } else {
                ProfileUserInfo profileUserInfo = new ProfileUserInfo(userId, fullNameStr, gender,
                        countryStr, universityStr, ageStr, languagesStr);
                db.collection("profiles").document(userId).set(profileUserInfo);
            }

        });
    }

    //to show toast message
    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

}
