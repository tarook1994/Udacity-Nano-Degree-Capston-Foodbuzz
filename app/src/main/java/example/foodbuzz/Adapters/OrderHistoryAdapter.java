package example.foodbuzz.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import example.foodbuzz.R;
import example.foodbuzz.data.OrderHistoryItem;

/**
 * Created by DELL on 4/6/2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    ArrayList<OrderHistoryItem> orderList;
    Context c;

    public OrderHistoryAdapter(Context c,ArrayList<OrderHistoryItem> orderList){
        this.c = c;
        this.orderList =orderList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView title,price,payment;
        CircleImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleHistory);
            payment = (TextView) view.findViewById(R.id.paymentHistory);
            price = (TextView) view.findViewById(R.id.priceHistory);
            icon = (CircleImageView) view.findViewById(R.id.OrderIcon);
        }


    }
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false);

        return new OrderHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryAdapter.MyViewHolder holder, int position) {
        holder.title.setText(orderList.get(position).getTitle());
        holder.payment.setText(orderList.get(position).getPayment());
        holder.price.setText(orderList.get(position).getPrice());
        Picasso.with(c).load(orderList.get(position).getIcon()).into(holder.icon);


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
