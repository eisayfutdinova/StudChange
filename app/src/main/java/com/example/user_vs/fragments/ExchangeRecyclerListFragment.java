package com.example.user_vs.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRecyclerListFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ExchangeRecyclerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_recycler_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //a list to store all the exchanges
        List<Exchange> exchangeList = new ArrayList<>();
      //  Exchange exchange = new Exchange("ВШЭ", "Программная инженерия","Россия", "русский");

/*
// Add a new document with a generated ID
        db.collection("exchange")
                .add(exchange)
                .addOnSuccessListener(documentReference -> Log.w(getActivity().getPackageName(), documentReference.getId()))
                .addOnFailureListener(e -> Log.w(getActivity().getPackageName(), "Cannot add data"));

*/

        //the recyclerview
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //creating recyclerview adapter
        ExchangeAdapter adapter = new ExchangeAdapter(getContext(), exchangeList);


        db.collection("exchange")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    exchangeList.addAll(queryDocumentSnapshots.toObjects(Exchange.class));

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.w(getActivity().getPackageName(), "Cannot get"));


    }
}
