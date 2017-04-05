package example.foodbuzz.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.OrderHistoryAdapter;
import example.foodbuzz.Adapters.RecyclerTouchListener;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.OrderHistoryItem;

public class MyOrdersActivity extends AppCompatActivity {
    RecyclerView orderList;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;
    ArrayList<OrderHistoryItem> historItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderList = (RecyclerView) findViewById(R.id.myOrder);
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        historItems= new ArrayList<>();
        Log.d("values1","here1");
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String restoredText = prefs.getString("email", null);
        Log.d("values2",restoredText);
        myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("values3","here2");
                Log.d("values4",dataSnapshot.toString());
                String values;
                values = dataSnapshot.getValue().toString();
                Log.d("values5",values);

                if(!values.equals("null")){
                    Log.d("values",values);
                    final String[] arrayValues = values.split(",");
                    myRef2.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                                for (int i = 0; i <arrayValues.length; i++) {
                                    if (friendSnapshot.getKey().equals(arrayValues[i])) {
                                        OrderHistoryItem item = new OrderHistoryItem(friendSnapshot.child("title")
                                                .getValue().toString(),friendSnapshot.child("price")
                                                .getValue().toString(),friendSnapshot.child("payment")
                                                .getValue().toString(),friendSnapshot.child("icon")
                                                .getValue().toString());
                                        historItems.add(item);
                                    }
                                }
                            }

                            OrderHistoryAdapter mAdapter = new OrderHistoryAdapter(getApplicationContext(),historItems);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            orderList.setLayoutManager(mLayoutManager);
                            orderList.setItemAnimator(new DefaultItemAnimator());
                            orderList.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
                            orderList.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                Log.d("values",values);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    }
