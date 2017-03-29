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
 * Created by DELL on 3/25/2017.
 */

public class CategoryAdapater extends RecyclerView.Adapter<CategoryAdapater.MyViewHolder> {
    String[] categoryArray;
    String[] categoryUrl;
    Context c;

    public CategoryAdapater(String[] categoryArray,String[] categoryUrl, Context c){

        this.categoryArray =categoryArray;
        this.categoryUrl = categoryUrl;
        this.c = c;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CatTitle;
        ImageView CatImg;

        public MyViewHolder(View view) {
            super(view);
            CatImg = (ImageView) view.findViewById(R.id.cat_img);
            CatTitle = (TextView) view.findViewById(R.id.cat_title);

        }
    }
    @Override
    public CategoryAdapater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rest_cat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(c).load(categoryUrl[position]).into(holder.CatImg);
        holder.CatTitle.setText(categoryArray[position]);
    }


    @Override
    public int getItemCount() {
        return categoryUrl.length;
    }
}