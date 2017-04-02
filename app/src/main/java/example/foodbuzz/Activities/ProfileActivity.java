package example.foodbuzz.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pitt.loadingview.library.LoadingView;

import example.foodbuzz.R;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout layout;
    LoadingView load;
    TextView name;
    EditText email;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        buildViews();

        myRef2.child("users").child(getIntent().getStringExtra("email").replace(".","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadInfoIntoViews(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void buildViews(){
        layout = (LinearLayout) findViewById(R.id.profile);
        load = (LoadingView) findViewById(R.id.loadingView);
        name = (TextView) findViewById(R.id.txt_edit_name);
        email = (EditText) findViewById(R.id.txt_edit_mail);
    }

    public void loadInfoIntoViews(DataSnapshot dataSnapshot){
        email.setText(dataSnapshot.child("email").getValue().toString());
        name.setText(dataSnapshot.child("username").getValue().toString());
        load.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);

    }
}
