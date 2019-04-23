package com.example.user_vs.fragments;


import android.Manifest;
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
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;

    private EditText userEmail, userPassword, userPassword2, userName;
    private ProgressBar loadingProgress;
    private Button regButton;

    private FirebaseAuth fbAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView regLogin = view.findViewById(R.id.reg_sign_in);

        userEmail = view.findViewById(R.id.reg_mail);
        userPassword = view.findViewById(R.id.reg_password);
        userPassword2 = view.findViewById(R.id.reg_confirm);
        userName = view.findViewById(R.id.reg_name);
        loadingProgress = view.findViewById(R.id.reg_progressBar);
        regButton = view.findViewById(R.id.reg_button);
        loadingProgress.setVisibility(View.INVISIBLE);
        pickedImgUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.drawable.registration);
        fbAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(v -> {
            regButton.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);

            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();
            final String password2 = userPassword2.getText().toString();
            final String name = userName.getText().toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || !password.equals(password2)) {
                showMessage("Please, verify all fields correctly");

                regButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
            } else {
                //creating users acount
                CreateUserAccount(email, name, password);
            }
        });

        regLogin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.auth_frame, new LogInFragment())
                    .commit();
        });

        ImgUserPhoto = view.findViewById(R.id.reg_usersPhoto);

        ImgUserPhoto.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestPermission();
            } else {
                openGallery();
            }
        });
    }

    private void CreateUserAccount(String email, String name, String password) {
        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                showMessage("Account created");

                //need to update his profile picture and name
                updateUserInfo(name, pickedImgUri, fbAuth.getCurrentUser());

            } else {
                showMessage("Account creation failed" + task.getException().getMessage());
                regButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
            }
        });

    }

    private void updateUserInfo(String name, Uri pickedImgUri, FirebaseUser currentUser) {
        //first update user's photo to firebase and get url

        StorageReference fbStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        StorageReference imageFilePath = fbStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {

            //get image url

            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                //url contains user image url

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();

                currentUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showMessage("Register Compete");
                                updateUI();
                            }
                        });
            });
        });

    }

    private void updateUI() {
        Intent homeActivity = new Intent(getContext(), MainActivity.class);
        homeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeActivity);
        getActivity().finish();
    }

    //to show toast message
    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            // user picked an image successfully
            // we need to save its reference to Uri variable
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);

        }
    }

    private void openGallery() {

        //open gallery intent and wait for users to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(), "Please, accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();

    }

}
