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

/**
 * Created by DELL on 4/3/2017.
 */

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.MyViewHolder> {
    ArrayList<String> titles,descriptions,icons,prices;
    Context c;


    public SubAdapter(Context c,ArrayList<String> titles,ArrayList<String> descriptions,ArrayList<String> icons,
                      ArrayList<String> prices){
        this.c = c;
        this.titles = titles;
        this.descriptions = descriptions;
        this.icons = icons;
        this.prices = prices;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView title,desc,price;
        ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title =(TextView)  view.findViewById(R.id.subTitle);
            desc =  (TextView) view.findViewById(R.id.subDesc);
            price = (TextView) view.findViewById(R.id.subPrice);
            icon = (ImageView) view.findViewById(R.id.subImg);
        }


    }

    @Override
    public SubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcat_item, parent, false);

        return new SubAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(c).load(icons.get(position)).into(holder.icon);
        holder.title.setText(titles.get(position));
        holder.desc.setText(descriptions.get(position));
        holder.price.setText(prices.get(position));

    }


    @Override
    public int getItemCount() {
        return titles.size();
    }
}
