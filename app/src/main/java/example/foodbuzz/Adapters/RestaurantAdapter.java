package example.foodbuzz.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import example.foodbuzz.R;


/**
 * Created by DELL on 3/23/2017.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {
    ArrayList<String> descArray,titleArray,thumbArray,priceArray,ratingArray;
    Context c;

    public RestaurantAdapter(ArrayList<String> descArray, ArrayList<String> titleArray, ArrayList<String> thumbArray,
                             ArrayList<String> priceArray, ArrayList<String> ratingArray, Context c){
        this.descArray = descArray;
        this.titleArray = titleArray;
        this.thumbArray = thumbArray;
        this.priceArray = priceArray;
        this.ratingArray = ratingArray;
        this.c = c;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView title,description;
        ImageView price;
        CircleImageView icon;

        public MyViewHolder(View view) {
            super(view);
             title = (TextView) view.findViewById(R.id.title);
             description = (TextView) view.findViewById(R.id.description);
             price = (ImageView) view.findViewById(R.id.price);
             icon = (CircleImageView) view.findViewById(R.id.RestImageView);
        }


    }
    @Override
    public RestaurantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rest_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RestaurantAdapter.MyViewHolder holder, int position) {
        String title = titleArray.get(position);
        String desc = descArray.get(position);
        String thumb = thumbArray.get(position);
        String price = priceArray.get(position);
        String rating = ratingArray.get(position);


        holder.title.setText(title);
        holder.description.setText(desc);
        if(price.equals("1")){
            Picasso.with(c).load(R.drawable.cheap).into(holder.price);
        } else {
            if(price.equals("2")){
                Picasso.with(c).load(R.drawable.average).into(holder.price);
            } else {
                Picasso.with(c).load(R.drawable.expensive).into(holder.price);
            }
        }
        Picasso.with(c).load(thumb).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return thumbArray.size();
    }
}
