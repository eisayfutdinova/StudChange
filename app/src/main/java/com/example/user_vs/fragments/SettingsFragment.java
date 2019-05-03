package com.example.user_vs.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;
import static com.example.user_vs.fragments.RegisterFragment.PReqCode;
import static com.example.user_vs.fragments.RegisterFragment.REQUESTCODE;

public class SettingsFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText current, newPassword, confirmPassword;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Настройки");


        current = view.findViewById(R.id.settings_currentPas);
        newPassword = view.findViewById(R.id.settings_newPas);
        confirmPassword = view.findViewById(R.id.settings_confirmPas);
        Button changePassword = view.findViewById(R.id.settings_changePassword);
        changePassword.setOnClickListener(v -> {
            changePassword();
        });


        Button deleteAccount = view.findViewById(R.id.settings_deleteAccount);
        ProgressBar progressBar = view.findViewById(R.id.settings_progressBar);
        deleteAccount.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this account will result in completely removing your account from system amd won't be able to access the app");
            dialog.setPositiveButton("Delete", (dialog1, which) -> {
                user.delete().addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Account deleted.", Toast.LENGTH_SHORT).show();
                        Intent regActivity = new Intent(getActivity(), AuthActivity.class);
                        startActivity(regActivity);
                        getActivity().finish();
                        return;
                    }else{
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            });

            dialog.setNegativeButton("Canel", (dialog12, which) -> {
               dialog12.dismiss();
            });

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

    }

    private void changePassword() {
        final String currentStr = current.getText().toString();
        final String newStr = newPassword.getText().toString();
        final String comfirmStr = confirmPassword.getText().toString();

        if (!currentStr.isEmpty() && !newStr.isEmpty() && !comfirmStr.isEmpty()) {
            if (!currentStr.equals(newStr)) {
                if (newStr.equals(comfirmStr)) {
                    if (user != null && user.getEmail() != null) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), currentStr);

// Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Re-Authentication success.", Toast.LENGTH_SHORT).show();
                                            user.updatePassword(newStr)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getActivity(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(getActivity(), "Password mismatching.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "New password should be different.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "Please enter all the fields.", Toast.LENGTH_SHORT).show();
        }
    }

}
