package com.example.user_vs.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRecyclerListFragment extends Fragment {

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


        //the recyclerview
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exchange")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        Exchange ex = ds.toObject(Exchange.class);
                        ex.setExchangeId(ds.getId());

                        exchangeList.add(ex);
                    }

                    //creating recyclerview adapter
                    ExchangeAdapter adapter = new ExchangeAdapter(getContext(), exchangeList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Log.w(getActivity().getPackageName(), "Cannot get"));

    }
}
