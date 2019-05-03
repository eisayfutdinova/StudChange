package com.example.user_vs.fragments;

import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ProductViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Exchange> productList;

    //getting the context and product list with constructor
    public ExchangeAdapter(Context mCtx, List<Exchange> productList) {
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
            ((MainActivity)mCtx).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, detailsExchangeFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewCountry, textViewShortDesc, textViewPrice, textViewType;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.card_Name);
            textViewShortDesc = itemView.findViewById(R.id.card_description);
            textViewType = itemView.findViewById(R.id.card_type);
            textViewPrice = itemView.findViewById(R.id.card_cost);
            textViewCountry = itemView.findViewById(R.id.card_country);
        }
    }
}
