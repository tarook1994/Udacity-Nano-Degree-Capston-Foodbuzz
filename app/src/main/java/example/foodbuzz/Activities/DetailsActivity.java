package example.foodbuzz.Activities;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.foodbuzz.Adapters.CategoryAdapater;
import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.RecyclerTouchListener;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.DBCart;
import example.foodbuzz.data.DBHandlerFav;
import example.foodbuzz.data.OrderItem;
import example.foodbuzz.data.Restaurant;

public class DetailsActivity extends AppCompatActivity {
    ImageView land;
    RecyclerView recyclerView;
    DBHandlerFav database;
    String[] cat;
    String[] catUrl;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference myRef2;
    String landImgFav;
    DBCart cart;
    ArrayList<OrderItem> order;

    @Override
    public void onBackPressed() {

        if(order.size()>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setMessage("By leaving this page your cart items will be deleted. Are you sure you want leave this page?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            cart.deleteOrder();
                            finish();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder.create();
            alert11.show();

        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = new DBHandlerFav(getApplicationContext());
        land = (ImageView) findViewById(R.id.land_image);
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        cart = new DBCart(getApplicationContext());
        order = cart.getAllOrder();
        if(getIntent().getStringExtra("cat")==null){
            firebasedatabase = FirebaseDatabase.getInstance();
            myRef2 = firebasedatabase.getReference();
            myRef2.child("restaurants").child(getIntent().getStringExtra("name")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                        cat =  dataSnapshot.child("Categories").getValue().toString().split(",");
                        catUrl = dataSnapshot.child("CatUrl").getValue().toString().split(",");
                        landImgFav = dataSnapshot.child("land").getValue().toString();
                        CategoryAdapater mAdapter = new CategoryAdapater(cat,catUrl,getApplication());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
                        recyclerView.setAdapter(mAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                                recyclerView,new RecyclerTouchListener.ClickListener() {

                        @Override
                        public void onClick(View view, int position) {
                            Intent i = new Intent(DetailsActivity.this,SubCategoryActivity.class);
                            i.putExtra("name",getIntent().getStringExtra("name"));
                            i.putExtra("cat",cat[position]);
                            startActivity(i);

                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                    Picasso.with(getApplicationContext()).load(landImgFav).into(land);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            cat = getIntent().getStringExtra("cat").split(",");
            catUrl = getIntent().getStringExtra("catUrl").split(",");
            CategoryAdapater mAdapter = new CategoryAdapater(cat,catUrl,getApplication());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                    recyclerView,new RecyclerTouchListener.ClickListener() {

                @Override
                public void onClick(View view, int position) {
                    Intent i = new Intent(DetailsActivity.this,SubCategoryActivity.class);
                    i.putExtra("name",getIntent().getStringExtra("name"));
                    Log.d("nameDetail",getIntent().getStringExtra("name"));
                    i.putExtra("thumb",getIntent().getStringExtra("icon"));
                    i.putExtra("cat",cat[position]);
                    startActivity(i);

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("land")).into(land);
        }



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDetails);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkIfFoundInDB()){
                    Restaurant res = new Restaurant(Integer.parseInt(getIntent().getStringExtra("position")),getIntent().getStringExtra("name"),
                            getIntent().getStringExtra("desc"),getIntent().getStringExtra("price"),
                            getIntent().getStringExtra("rating"),getIntent().getStringExtra("icon"));

                    DBHandlerFav db = new DBHandlerFav(getApplicationContext());
                    db.addRow(res);
                    fab.setImageDrawable(getDrawable(R.drawable.heartlast));
                    Snackbar.make(view, "Added to Favorates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    fab.setImageDrawable(getDrawable(R.drawable.white));
                    database.delete(getIntent().getStringExtra("name"));
                    Snackbar.make(view, "Removed from Favorates", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        if(checkIfFoundInDB()){
            fab.setImageDrawable(getDrawable(R.drawable.heartlast));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(order.size()>0){
            cart.deleteOrder();
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    public boolean checkIfFoundInDB(){
        boolean nameFound = false;
        String[] result_columns = new String[] {
                DBHandlerFav.KEY_NAME};
// Specify the where clause that will limit our results.
        String where = null;
// Replace these with valid SQL statements as necessary.
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.query(DBHandlerFav.TABLE_FAV,
                result_columns, where, whereArgs, groupBy, having, order);
        int index = cursor.getColumnIndexOrThrow(DBHandlerFav.KEY_NAME);
        while (cursor.moveToNext()) {

            Log.d("hey",cursor.getString(index) + "");
            if(cursor.getString(index).equals(getIntent().getStringExtra("name"))){
                Log.d("ana gowa","la2eto");
                nameFound=true;
            }


        }
        return nameFound;
    }
}
