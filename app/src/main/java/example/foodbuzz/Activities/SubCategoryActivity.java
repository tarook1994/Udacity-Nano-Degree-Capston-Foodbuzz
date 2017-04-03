package example.foodbuzz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pitt.loadingview.library.LoadingView;

import java.util.ArrayList;

import example.foodbuzz.Adapters.CategoryAdapater;
import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.RecyclerTouchListener;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.Adapters.SubAdapter;
import example.foodbuzz.R;

public class SubCategoryActivity extends AppCompatActivity {
    LoadingView load;
    RecyclerView sub;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference myRef2;
    ArrayList<String> titles,descriptions,icons,prices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        buildViews();
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef2 = firebasedatabase.getReference();
        myRef2.child("restaurants").child(getIntent().getStringExtra("name")).child(getIntent().getStringExtra("cat"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                load.setVisibility(View.GONE);

                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    titles.add(friendSnapshot.getKey());
                    descriptions.add(friendSnapshot.child("desc").getValue().toString());
                    icons.add(friendSnapshot.child("img").getValue().toString());
                    prices.add(friendSnapshot.child("price").getValue().toString());

                }
                addListenerToRecycler();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void buildViews(){
        load = (LoadingView) findViewById(R.id.Loading);
        sub = (RecyclerView) findViewById(R.id.subcat);
        titles = new ArrayList<>();
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
        icons = new ArrayList<>();

    }

    public void addListenerToRecycler(){
        SubAdapter mAdapter = new SubAdapter(getApplicationContext(),titles,descriptions,icons,prices);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sub.setLayoutManager(mLayoutManager);
        sub.setItemAnimator(new DefaultItemAnimator());
        sub.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
        sub.setAdapter(mAdapter);
        sub.setVisibility(View.VISIBLE);
        sub.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                sub,new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
