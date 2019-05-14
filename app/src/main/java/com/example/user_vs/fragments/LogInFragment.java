package com.example.user_vs.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {

    private EditText userEmail, userPassword;
    private ProgressBar loadingProgress;
    private ImageView loginButton, logRegister;

    TextView resetPassword, noRegistration;
    Typeface typeface;

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
        typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Regular.ttf");

        userEmail = view.findViewById(R.id.login_email);
        userPassword = view.findViewById(R.id.login_password);
        loadingProgress = view.findViewById(R.id.login_progressBar);
        loginButton = view.findViewById(R.id.login_button);

        logRegister = view.findViewById(R.id.log_register);
        noRegistration = view.findViewById(R.id.log_NoRegistration);
        resetPassword = view.findViewById(R.id.login_resetPassword);

        resetPassword.setTypeface(typeface);
        noRegistration.setTypeface(typeface);
        userEmail.setTypeface(typeface);

        buttonClickTrue();
        fbAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(c -> {
            buttonClickFalse();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Forgot password");
            View view1 = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
            EditText username = view1.findViewById((R.id.forgot_userMail));
            builder.setView(view1);
            builder.setPositiveButton("Reset", (dialog, which) -> {
                buttonClickTrue();
                forgotPassword(username);
            });
            builder.setNegativeButton("Close", (dialog, which) -> {
                buttonClickTrue();
            });

            builder.show();
            buttonClickTrue();
        });

        loginButton.setOnClickListener(v -> {
            buttonClickFalse();

            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showMessage("Please, verify all fields correctly");

                buttonClickTrue();
            } else {
                CheckAccount(email, password);
                loadingProgress.setVisibility(View.VISIBLE);
            }

        });

        logRegister.setOnClickListener(v -> {
            buttonClickFalse();

            Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_frame, new RegisterFragment())
                    .commit();

        });

        noRegistration.setOnClickListener(v -> {
            buttonClickFalse();
            loadingProgress.setVisibility(View.VISIBLE);

            fbAuth.signInAnonymously().addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success");
                    updateUI();

                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    loadingProgress.setVisibility(View.INVISIBLE);
                    buttonClickTrue();
                }
            });
        });
    }

    private void forgotPassword(EditText username) {
        if (username.getText().toString().isEmpty()) {
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()) {
            return;
        }
        fbAuth.sendPasswordResetEmail(username.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Email sent.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CheckAccount(String email, String password) {
        fbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loadingProgress.setVisibility(View.INVISIBLE);
                updateUI();
            } else {
                showMessage(Objects.requireNonNull(task.getException()).getMessage());
                buttonClickTrue();
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getContext(), MainActivity.class);
        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeActivity);
        getActivity().finish();
    }

    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    private void buttonClickTrue() {
        loginButton.setClickable(true);
        logRegister.setClickable(true);
        resetPassword.setClickable(true);
        noRegistration.setClickable(true);
    }

    private void buttonClickFalse() {
        loginButton.setClickable(false);
        logRegister.setClickable(false);
        resetPassword.setClickable(false);
        noRegistration.setClickable(false);
    }
}
