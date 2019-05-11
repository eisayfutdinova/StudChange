package com.example.user_vs.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForAnonymousFragment extends Fragment {

    private ImageView signinButton, signupButton;

    public ForAnonymousFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_for_anonymous, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signinButton = view.findViewById(R.id.guest_sign_in);
        signupButton = view.findViewById(R.id.guest_sign_up);

        signinButton.setOnClickListener(v ->{
            Intent i = new Intent(getActivity(), AuthActivity.class);
            i.putExtra("key", true);
            startActivity(i);
        });

        signupButton.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), AuthActivity.class);
            i.putExtra("key", false);
            startActivity(i);
        });

    }
}
