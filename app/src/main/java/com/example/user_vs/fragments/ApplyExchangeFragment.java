package com.example.user_vs.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.Authenticator;
import java.util.BitSet;
import java.util.Properties;


public class ApplyExchangeFragment extends Fragment {

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;

    public ApplyExchangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        EditText apply_name, apply_country, apply_type, apply_cost, apply_link, apply_desc, apply_email;
        Button applyBtn;

        apply_name = view.findViewById(R.id.apply_name);
        apply_cost = view.findViewById(R.id.apply_costInformation);
        apply_country = view.findViewById(R.id.apply_county);
        apply_type = view.findViewById(R.id.apply_type);
        apply_link = view.findViewById(R.id.apply_link);
        apply_desc = view.findViewById(R.id.apply_description);
        apply_email = view.findViewById(R.id.apply_email);
        applyBtn = view.findViewById(R.id.apply_button);

        applyBtn.setOnClickListener(v->{
            final String apply_nameStr = apply_name.getText().toString();
            final String apply_costStr = apply_cost.getText().toString();
            final String apply_countryStr = apply_country.getText().toString();
            final String apply_typeStr = apply_type.getText().toString();
            final String apply_linkStr = apply_link.getText().toString();
            final String apply_descStr = apply_desc.getText().toString();
            final String apply_emailStr = apply_email.getText().toString();
            ModelOfApply modelOfApply = new ModelOfApply(apply_nameStr,apply_costStr,apply_countryStr,apply_typeStr,apply_linkStr,apply_descStr,apply_emailStr);

            if (apply_nameStr.isEmpty() || apply_costStr.isEmpty() || apply_countryStr.isEmpty() || apply_typeStr.isEmpty() || apply_linkStr.isEmpty() || apply_descStr.isEmpty() || apply_emailStr.isEmpty()) {
                showMessage("Please, verify all fields correctly");
            } else {
                db.collection("applies")
                        .document()
                        .set(modelOfApply);
            }
        });
    }

    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

}

