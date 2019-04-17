package com.example.user_vs.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {

    private Intent MainActivity;

    private EditText userEmail, userPassword;
    private ProgressBar loadingProgress;
    private Button loginButton;
    private TextView logRegister;

    private FirebaseAuth fbAuth;


    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userEmail = view.findViewById(R.id.login_email);
        userPassword = view.findViewById(R.id.login_password);
        loadingProgress = view.findViewById(R.id.login_progressBar);
        loginButton = view.findViewById(R.id.login_button);
        logRegister = view.findViewById(R.id.log_register);

        fbAuth = FirebaseAuth.getInstance();
        MainActivity = new Intent(getContext(),MainActivity.class);

        loginButton.setOnClickListener(v -> {
            loginButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showMessage("Please, verify all fields correctly");

                loginButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
            } else {
                CheckAccount(email, password);
            }

        });

        logRegister.setOnClickListener(v ->{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_frame, new RegisterFragment())
                    .commit();
        });
    }

    private void CheckAccount(String email, String password) {
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingProgress.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    updateUI();
                }else{
                    showMessage(task.getException().getMessage());
                    loginButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateUI() {
        startActivity(MainActivity);
        getActivity().finish();
    }

    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }
}
