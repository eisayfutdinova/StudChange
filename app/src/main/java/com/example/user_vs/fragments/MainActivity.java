package com.example.user_vs.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.example.user_vs.fragments.RegisterFragment.PReqCode;
import static com.example.user_vs.fragments.RegisterFragment.REQUESTCODE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Uri pickedImgUri;
    ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_exchange_list);

        View navViewHeader = navigationView.getHeaderView(0);

        TextView userName = navViewHeader.findViewById(R.id.userName);
        TextView userMail = navViewHeader.findViewById(R.id.userMail);
        userPhoto = navViewHeader.findViewById(R.id.userPhoto);

        if (user.getPhotoUrl() != null)
            Glide.with(this).load(user.getPhotoUrl()).into(userPhoto);

        userPhoto.setOnClickListener(v->{
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestPermission();
            } else {
                openGallery();
            }
        });

        userName.setText(user.getDisplayName());
        userMail.setText(user.getEmail());


        //add this line to display menu1 when the activity is loaded
    }

    private void updateUserInfo( Uri pickedImgUri, FirebaseUser currentUser) {
        //first update user's photo to firebase and get url

        StorageReference fbStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        StorageReference imageFilePath = fbStorage.child(pickedImgUri.getLastPathSegment());

        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            //get image url

            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                //url contains user image url

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build();

                currentUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                            }
                        });
            });
        });

    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Please, accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            // user picked an image successfully
            // we need to save its reference to Uri variable
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);
            updateUserInfo(pickedImgUri, user);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_profile:
                if (!user.isAnonymous()) {
                    fragment = new ProfileFragment();
                    break;
                }
                fragment = new ForAnonymousFragment();
                break;
            case R.id.nav_liked:
                if (!user.isAnonymous()) {
                    fragment = new LikedFragment();
                    break;
                }
                fragment = new ForAnonymousFragment();
                break;
            case R.id.nav_request:
                if (!user.isAnonymous()) {
                    fragment = new RequsetFragment();
                    break;
                }
                fragment = new ForAnonymousFragment();
                break;

            case R.id.nav_exchange_list:
                fragment = new ExchangeRecyclerListFragment();
                break;

            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_logout:
                Intent regActivity = new Intent(this, AuthActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(regActivity);
                finish();
                return;
            case R.id.nav_apply_ex:
                fragment = new ApplyExchangeFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
