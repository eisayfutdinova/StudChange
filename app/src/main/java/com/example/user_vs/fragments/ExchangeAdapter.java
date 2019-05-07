package com.example.user_vs.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ProductViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Exchange> productList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //getting the context and product list with constructor
    ExchangeAdapter(Context mCtx, List<Exchange> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Exchange product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getName());
        holder.textViewShortDesc.setText(product.getDescription());
        holder.textViewPrice.setText(product.getCost());
        holder.textViewType.setText(product.getType());
        holder.textViewCountry.setText(product.getCountry());
        holder.itemView.setOnClickListener(v -> {
            DetailsExchangeFragment detailsExchangeFragment = DetailsExchangeFragment.newInstance(product.getExchangeId());
            ((MainActivity) mCtx).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, detailsExchangeFragment)
                    .addToBackStack(null)
                    .commit();
        });


        db.collection("likes")
                .whereEqualTo("userId", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String exchange = (String) document.getData().get("exchangeId");
                            if (product.getExchangeId().equals(exchange))
                                holder.likeButton.setLiked(true);
                        }
                    }
                });

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                UserLikes userLikes = new UserLikes(user.getUid(), product.getExchangeId());
                db.collection("likes")
                        .document()
                        .set(userLikes);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                db.collection("likes")
                        .whereEqualTo("userId", user.getUid())
                        .whereEqualTo("exchangeId", product.getExchangeId())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String likedDoc = task.getResult().getDocuments().get(0).getId();

                                db.collection("likes")
                                        .document(likedDoc)
                                        .delete();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewCountry, textViewShortDesc, textViewPrice, textViewType;
        LikeButton likeButton;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.card_Name);
            textViewShortDesc = itemView.findViewById(R.id.card_description);
            textViewType = itemView.findViewById(R.id.card_type);
            textViewPrice = itemView.findViewById(R.id.card_cost);
            textViewCountry = itemView.findViewById(R.id.card_country);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }
}
