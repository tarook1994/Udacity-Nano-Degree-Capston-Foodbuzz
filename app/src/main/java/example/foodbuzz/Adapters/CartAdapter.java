package example.foodbuzz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.foodbuzz.R;
import example.foodbuzz.data.OrderItem;

/**
 * Created by DELL on 4/4/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    ArrayList<OrderItem> order;
    Context c;
    double total=0;

    public CartAdapter(Context c,ArrayList<OrderItem> order){
        this.c = c;
        this.order = order;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemPrice,cartItemTotalPrice,cartTotal;
        ImageView cartImg;

        public MyViewHolder(View view) {
            super(view);
            cartImg = (ImageView) view.findViewById(R.id.cartItemImg);
            cartItemPrice = (TextView) view.findViewById(R.id.cartItemPrice);
            cartItemTotalPrice = (TextView) view.findViewById(R.id.cartItemTotalPrice);

            cartTotal = (TextView) view.findViewById(R.id.cartTotal);


        }
    }
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {
        OrderItem item = order.get(position);
        holder.cartItemPrice.setText(item.getPrice()+" $ x "+item.getNumber());
        double finalPrice = Integer.parseInt(item.getNumber())*Double.parseDouble(item.getPrice().replace("$",""));
        total += finalPrice;
        holder.cartItemTotalPrice.setText(finalPrice+" $");
        Picasso.with(c).load(order.get(position).getUrl()).into(holder.cartImg);


    }

    @Override
    public int getItemCount() {
        return order.size();
    }
}
