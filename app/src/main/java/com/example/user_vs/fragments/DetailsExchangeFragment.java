package com.example.user_vs.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsExchangeFragment extends Fragment {


    public DetailsExchangeFragment() {
        // Required empty public constructor
    }

    public static DetailsExchangeFragment newInstance(String exchangeId) {
        DetailsExchangeFragment detailsExchangeFragment = new DetailsExchangeFragment();

        Bundle args = new Bundle();
        args.putString("exchange_id", exchangeId);
        detailsExchangeFragment.setArguments(args);
        return detailsExchangeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String ex_id = args.getString("exchange_id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exchange").document(ex_id).get()
                .addOnSuccessListener(queryDocSnap -> {
                    Exchange exchange = queryDocSnap.toObject(Exchange.class);
                    ((TextView)getActivity().findViewById(R.id.exchange_headline)).setText(exchange.getUnivercityName());
                    ((TextView)getActivity().findViewById(R.id.countyTextView)).setText(exchange.getCountry());
                    ((TextView)getActivity().findViewById(R.id.programTextView)).setText(exchange.getEducationalProgram());
                    ((TextView)getActivity().findViewById(R.id.informationTextView)).setText(exchange.getLanguage());
                });

    }
}
