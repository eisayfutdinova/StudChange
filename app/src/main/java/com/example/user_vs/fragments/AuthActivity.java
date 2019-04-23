package com.example.user_vs.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AuthActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent intent = getIntent();
        Boolean value = intent.getBooleanExtra("key", true);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.isAnonymous()) {
            if (value) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_frame, new LogInFragment())
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_frame, new RegisterFragment())
                        .commit();
            }
        } else {
            Intent regActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(regActivity);
            finish();
            return;
        }

    }

}
