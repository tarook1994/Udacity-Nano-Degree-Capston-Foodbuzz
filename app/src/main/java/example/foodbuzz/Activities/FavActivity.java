package example.foodbuzz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.RecyclerTouchListener;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.DBHandlerFav;
import example.foodbuzz.data.Restaurant;

public class FavActivity extends AppCompatActivity {
    DBHandlerFav database;
    ArrayList<String> descArray,titleArray,thumbArray,priceArray,ratingArray,catUrl,land,cat;
    RecyclerView rest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rest = (RecyclerView) findViewById(R.id.fav_list);
        database = new DBHandlerFav(getApplicationContext());
        catUrl = new ArrayList<>();
        land = new ArrayList<>();
        cat = new ArrayList<>();
        List<Restaurant> list = database.getAllFavList();
        addDataToArrayLists(list);
        RestaurantAdapter mAdapter = new RestaurantAdapter(descArray,titleArray,thumbArray,priceArray,ratingArray,getApplication());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rest.setLayoutManager(mLayoutManager);
        rest.setItemAnimator(new DefaultItemAnimator());
        rest.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rest.setAdapter(mAdapter);

        rest.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rest,new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                String titlePressed = titleArray.get(position);
                descArray = getIntent().getStringArrayListExtra("desc");
                titleArray = getIntent().getStringArrayListExtra("name");
                thumbArray = getIntent().getStringArrayListExtra("icon");
                priceArray = getIntent().getStringArrayListExtra("price");
                ratingArray = getIntent().getStringArrayListExtra("rating");
                catUrl = getIntent().getStringArrayListExtra("catUrl");
                cat = getIntent().getStringArrayListExtra("cat");
                land = getIntent().getStringArrayListExtra("land");

                for(int i = 0;i<titleArray.size();i++){
                    if(titlePressed.equals(titleArray.get(i))){
                        Intent in = new Intent(FavActivity.this,DetailsActivity.class);
                        in.putExtra("name",titleArray.get(i));
                        in.putExtra("position",position);
                        in.putExtra("icon",thumbArray.get(i));
                        in.putExtra("price",priceArray.get(i));
                        in.putExtra("rating",ratingArray.get(i));
                        in.putExtra("cat",cat.get(i));
                        in.putExtra("catUrl",catUrl.get(i));
                        in.putExtra("land",land.get(i));
                        startActivity(in);

                    }
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }
    public void addDataToArrayLists(List<Restaurant> list){
        descArray = new ArrayList<>();
        titleArray = new ArrayList<>();
        thumbArray = new ArrayList<>();
        priceArray = new ArrayList<>();
        ratingArray = new ArrayList<>();


        for(int i = 0;i<list.size();i++){
            descArray.add(list.get(i).getDescription());
            titleArray.add(list.get(i).getName());
            thumbArray.add(list.get(i).getIcon());
            priceArray.add(list.get(i).getPrice());
            ratingArray.add(list.get(i).getRating());
            Log.d("icon",list.get(i).getIcon());

        }

    }
}
