package com.example.user_vs.fragments;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LikedFragment extends Fragment {

    private List<Exchange> likedExchangeList;
    private RecyclerView recyclerView;
    private Typeface typeface;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.liked_fragment, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Сохраненные стажировки");
        typeface = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Rubik-Regular.ttf");

        TextView textView = view.findViewById(R.id.textViewForLikes);
        textView.setTypeface(typeface);

        //a list to store all the exchanges
        likedExchangeList = new ArrayList<>();
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.liked_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("likes")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty())
                            textView.setVisibility(View.VISIBLE);
                        else {
                            textView.setVisibility(View.INVISIBLE);

                            List<UserLikes> exIds = task.getResult().toObjects(UserLikes.class);

                            db.collection("exchange")
                                    .get()
                                    .addOnCompleteListener(tasks -> {
                                        if (tasks.isSuccessful()) {

                                            for(QueryDocumentSnapshot item : tasks.getResult()) {
                                                Exchange exchange = item.toObject(Exchange.class);
                                                exchange.setExchangeId(item.getId());
                                                likedExchangeList.add(exchange);
                                            }

                                            List<Exchange> koroche = new ArrayList<>();

                                            for (Exchange exchange : likedExchangeList) {
                                                for (UserLikes userLikes : exIds) {
                                                    if (exchange.getExchangeId().equals(userLikes.getExchangeId()))
                                                        koroche.add(exchange);
                                                }
                                            }

                                            ExchangeAdapter adapter = new ExchangeAdapter(getContext(), koroche);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    });

                        }
                    }
                });
    }
}
