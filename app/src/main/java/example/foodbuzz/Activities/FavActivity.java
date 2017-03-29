package example.foodbuzz.Activities;

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
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.DBHandlerFav;
import example.foodbuzz.data.Restaurant;

public class FavActivity extends AppCompatActivity {
    DBHandlerFav database;
    ArrayList<String> descArray,titleArray,thumbArray,priceArray,ratingArray;
    RecyclerView rest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rest = (RecyclerView) findViewById(R.id.fav_list);
        database = new DBHandlerFav(getApplicationContext());
        List<Restaurant> list = database.getAllFavList();
        addDataToArrayLists(list);
        RestaurantAdapter mAdapter = new RestaurantAdapter(descArray,titleArray,thumbArray,priceArray,ratingArray,getApplication());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rest.setLayoutManager(mLayoutManager);
        rest.setItemAnimator(new DefaultItemAnimator());
        rest.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rest.setAdapter(mAdapter);


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
