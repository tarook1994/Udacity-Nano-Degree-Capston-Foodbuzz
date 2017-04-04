package example.foodbuzz.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import example.foodbuzz.data.DBCart;
import example.foodbuzz.data.OrderItem;

public class SubCategoryActivity extends AppCompatActivity {
    LoadingView load;
    RecyclerView sub;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference myRef2;
    ArrayList<String> titles,descriptions,icons,prices;
    FloatingActionButton fab;
    DBCart cart;
    ArrayList<OrderItem> order;

    @Override
    protected void onResume() {
        super.onResume();
        Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_anim);
        fab.startAnimation(animation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        buildViews();
        cart = new DBCart(getApplicationContext());
        order = new ArrayList<>();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    fabClick();
            }
        });


    }

    public void fabClick(){
        order = cart.getAllOrder();
        for(int i =0;i<order.size();i++){
            Toast.makeText(this, "Found Item : "+order.get(i).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    public void buildViews(){
        load = (LoadingView) findViewById(R.id.Loading);
        sub = (RecyclerView) findViewById(R.id.subcat);
        fab = (FloatingActionButton) findViewById(R.id.cart);
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
                Intent i = new Intent(SubCategoryActivity.this,ChooseItemActivity.class);
                i.putExtra("title",titles.get(position));
                i.putExtra("desc",descriptions.get(position));
                i.putExtra("price",prices.get(position));
                i.putExtra("icon",icons.get(position));
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
}
