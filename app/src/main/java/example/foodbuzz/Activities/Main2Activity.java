package example.foodbuzz.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.RecyclerTouchListener;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.DBHandler;
import example.foodbuzz.data.Restaurant;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> desc,thumbPhoto,landPhoto,price,rating,title,category,categoryUrl;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;
    RecyclerView rest;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);


        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        desc = new ArrayList<>();
        thumbPhoto =new ArrayList<>();
        landPhoto = new ArrayList<>();
        price = new ArrayList<>();
        title = new ArrayList<>();
        rating = new ArrayList<>();
        category = new ArrayList<>();
        categoryUrl = new ArrayList<>();

        //Getting data from DB
        getDataFromDatabaseIntoArrays();

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main2Activity.this,FavActivity.class);
                i.putStringArrayListExtra("name",title);
                Log.d("title size",title.size()+"");
                i.putStringArrayListExtra("cat",category);
                i.putStringArrayListExtra("land",landPhoto);
                i.putStringArrayListExtra("desc",desc);
                i.putStringArrayListExtra("icon",thumbPhoto);
                i.putStringArrayListExtra("catUrl",categoryUrl);
                i.putStringArrayListExtra("rating",rating);
                i.putStringArrayListExtra("price",price);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up,R.anim.fade_out);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        rest = (RecyclerView) findViewById(R.id.restaurant_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        for(int i = 0;i<navigationView.getHeaderCount();i++){
            navigationView.getMenu().getItem(i).setChecked(false);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void getDataFromDatabaseIntoArrays(){
        myRef2.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative);
                layout.setVisibility(View.GONE);
                DBHandler db = new DBHandler(getApplicationContext());
                SharedPreferences.Editor editor = getSharedPreferences("DBCheck", MODE_PRIVATE).edit();
                int counter= 0;
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    title.add(friendSnapshot.getKey());
                    desc.add(friendSnapshot.child("desc").getValue().toString());
                    thumbPhoto.add(friendSnapshot.child("thumb").getValue().toString());
                    landPhoto.add(friendSnapshot.child("land").getValue().toString());
                    rating.add(friendSnapshot.child("rating").getValue().toString());
                    price.add(friendSnapshot.child("price").getValue().toString());
                    categoryUrl.add(friendSnapshot.child("CatUrl").getValue().toString());
                    category.add(friendSnapshot.child("Categories").getValue().toString());
                    if(!getSharedPreferences("DBCheck", MODE_PRIVATE).contains("check")){
                        Restaurant res = new Restaurant(counter,friendSnapshot.getKey()
                                ,friendSnapshot.child("desc").getValue().toString(),
                                friendSnapshot.child("price").getValue().toString(),
                                friendSnapshot.child("rating").getValue().toString());
                        db.addRow(res);
                        editor.putString("check", "true");
                        editor.apply();
                        counter++;
                    }


                }

                RestaurantAdapter mAdapter = new RestaurantAdapter(desc,title,thumbPhoto,price,rating,getApplication());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rest.setLayoutManager(mLayoutManager);
                rest.setItemAnimator(new DefaultItemAnimator());
                rest.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));

                rest.setAdapter(mAdapter);
                rest.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                fab.animate().alpha(1.0f).setDuration(100);
                rest.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                        rest,new RecyclerTouchListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(Main2Activity.this,DetailsActivity.class);
                        i.putExtra("land",landPhoto.get(position));
                        i.putExtra("cat",category.get(position));
                        i.putExtra("name",title.get(position));
                        i.putExtra("desc",desc.get(position));
                        i.putExtra("icon",thumbPhoto.get(position));
                        i.putExtra("rating",rating.get(position));
                        i.putExtra("price",price.get(position));
                        i.putExtra("position",position+"");
                        Log.d("cat",category.get(position));
                        Log.d("catUrl",categoryUrl.get(position));
                        Log.d("Baft7","hena");

                        i.putExtra("catUrl",categoryUrl.get(position));
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            Intent i = new Intent(Main2Activity.this,ProfileActivity.class);
            i.putExtra("email",getIntent().getStringExtra("email"));
            startActivity(i);

        } else if (id == R.id.my_orders) {
            Intent i = new Intent(Main2Activity.this,MyOrdersActivity.class);
            startActivity(i);

        }  else if (id == R.id.sign_out) {
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
