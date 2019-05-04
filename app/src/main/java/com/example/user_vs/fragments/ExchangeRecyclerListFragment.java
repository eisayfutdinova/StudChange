package com.example.user_vs.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRecyclerListFragment extends Fragment {

    ArrayList<String> listOfProgram;
    ArrayList<String> listOfCountries;

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
        List<Exchange> exchangePrograms = new ArrayList<>();
        List<Exchange> exchangeCountries = new ArrayList<>();

        listOfProgram = new ArrayList<>();
        listOfCountries = new ArrayList<>();

        //the recyclerview
        RecyclerView recyclerView;
        recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exchange")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        Exchange ex = ds.toObject(Exchange.class);
                        assert ex != null;
                        ex.setExchangeId(ds.getId());
                        exchangeList.add(ex);
                    }

                    //creating recyclerview adapter
                    ExchangeAdapter adapter = new ExchangeAdapter(getContext(), exchangeList);
                    recyclerView.setAdapter(adapter);

                    Button filterProgramButton = view.findViewById(R.id.filter_program);
                    TextView selectedProgramView = view.findViewById(R.id.filter_selected_programs);
                    HashSet<String> setOfTypes = new LinkedHashSet<>();
                    List<Exchange> exchange_programs = new ArrayList<>();
                    for (Exchange types : exchangeList) {
                        setOfTypes.add(types.getType());
                    }
                    listOfProgram.addAll(setOfTypes);
                    String[] arrayOfprograms = new String[listOfProgram.size()];
                    listOfProgram.toArray(arrayOfprograms);
                    boolean[] cheakedItems = new boolean[arrayOfprograms.length];
                    ArrayList<Integer> selectedPrograms = new ArrayList<>();

                    filterProgramButton.setOnClickListener(v -> {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle(R.string.dialog_programs);
                        mBuilder.setMultiChoiceItems(arrayOfprograms, cheakedItems, (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedPrograms.add(which);
                            } else {
                                selectedPrograms.remove((Integer.valueOf(which)));
                            }
                        });

                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton(getString(R.string.ok_label), (dialog, which) -> {
                            StringBuilder lang = new StringBuilder();
                            for (int i = 0; i < selectedPrograms.size(); i++) {
                                lang.append(arrayOfprograms[selectedPrograms.get(i)]);
                                if (i != selectedPrograms.size() - 1) {
                                    lang.append(", ");
                                }
                            }
                            selectedProgramView.setText(lang.toString());
                            exchange_programs.clear();
                            if (!exchangeCountries.isEmpty()) {
                                for (Exchange ex : exchangeCountries) {
                                    for (int i = 0; i < selectedPrograms.size(); i++) {
                                        if (ex.getType().equals(arrayOfprograms[selectedPrograms.get(i)]))
                                            exchange_programs.add(ex);
                                    }
                                }
                            } else {
                                for (Exchange ex : exchangeList) {
                                    for (int i = 0; i < selectedPrograms.size(); i++) {
                                        if (ex.getType().equals(arrayOfprograms[selectedPrograms.get(i)]))
                                            exchange_programs.add(ex);
                                    }
                                }
                            }

                            recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchange_programs));
                            recyclerView.getAdapter().notifyDataSetChanged();

                        });

                        mBuilder.setNegativeButton(R.string.dismiss_label, (dialog, which) -> dialog.dismiss());

                        mBuilder.setNeutralButton(R.string.clear_all_label, (dialog, which) -> {
                            for (int i = 0; i < cheakedItems.length; i++) {
                                cheakedItems[i] = false;
                                selectedPrograms.clear();
                                selectedProgramView.setText("");
                            }
                            if (!exchangeCountries.isEmpty()) {
                                recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchangeCountries));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(adapter);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        });

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    });


                    Button filterCountryButton = view.findViewById(R.id.filter_country);
                    TextView selectedCountiesView = view.findViewById(R.id.filter_selected_country);
                    HashSet<String> setOfCountries = new LinkedHashSet<>();
                    for (Exchange types : exchangeList) {
                        setOfCountries.add(types.getCountry());
                    }
                    listOfCountries.addAll(setOfCountries);

                    String[] arrayOfCountries = new String[listOfCountries.size()];
                    listOfCountries.toArray(arrayOfCountries);
                    boolean[] cheakedItems1 = new boolean[arrayOfCountries.length];
                    ArrayList<Integer> selectedCountries = new ArrayList<>();

                    filterCountryButton.setOnClickListener(v -> {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        mBuilder.setTitle(R.string.dialog_countries);
                        mBuilder.setMultiChoiceItems(arrayOfCountries, cheakedItems1, (dialog, which, isChecked) -> {
                            if (isChecked) {
                                selectedCountries.add(which);
                            } else {
                                selectedCountries.remove((Integer.valueOf(which)));
                            }
                        });

                        mBuilder.setCancelable(false);
                        mBuilder.setPositiveButton(getString(R.string.ok_label), (dialog, which) -> {
                            StringBuilder lang = new StringBuilder();
                            for (int i = 0; i < selectedCountries.size(); i++) {
                                lang.append(arrayOfCountries[selectedCountries.get(i)]);
                                if (i != selectedCountries.size() - 1) {
                                    lang.append(", ");
                                }
                            }
                            selectedCountiesView.setText(lang.toString());
                            exchangeCountries.clear();
                            if (!exchange_programs.isEmpty()) {
                                for (Exchange ex : exchange_programs) {
                                    for (int i = 0; i < selectedCountries.size(); i++) {
                                        if (ex.getCountry().equals(arrayOfCountries[selectedCountries.get(i)]))
                                            exchangeCountries.add(ex);
                                    }
                                }
                            } else {
                                for (Exchange ex : exchangeList) {
                                    for (int i = 0; i < selectedCountries.size(); i++) {
                                        if (ex.getCountry().equals(arrayOfCountries[selectedCountries.get(i)]))
                                            exchangeCountries.add(ex);
                                    }
                                }
                            }

                            recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchangeCountries));
                            recyclerView.getAdapter().notifyDataSetChanged();

                        });

                        mBuilder.setNegativeButton(R.string.dismiss_label, (dialog, which) -> dialog.dismiss());

                        mBuilder.setNeutralButton(R.string.clear_all_label, (dialog, which) -> {
                            for (int i = 0; i < cheakedItems1.length; i++) {
                                cheakedItems1[i] = false;
                                selectedCountries.clear();
                                selectedCountiesView.setText("");
                            }
                            if (!exchange_programs.isEmpty()) {
                                recyclerView.setAdapter(new ExchangeAdapter(getContext(), exchange_programs));
                                recyclerView.getAdapter().notifyDataSetChanged();
                            } else {
                                recyclerView.setAdapter(adapter);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }

                        });

                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();
                    });

                })
                .addOnFailureListener(e -> Log.w(getActivity().getPackageName(), "Cannot get"));


    }
}
