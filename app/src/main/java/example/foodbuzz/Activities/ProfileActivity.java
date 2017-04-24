package example.foodbuzz.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pitt.loadingview.library.LoadingView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import example.foodbuzz.R;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout layout;
    LoadingView load;
    TextView name,nameprofile;
    EditText email;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;
    FloatingActionButton fab;
    CircleImageView imge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        buildViews();
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String restoredText = prefs.getString("email", null);

        myRef2.child("users").child(restoredText.replace(".","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadInfoIntoViews(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void buildViews(){
        layout = (LinearLayout) findViewById(R.id.profile);
        load = (LoadingView) findViewById(R.id.loadingView);
        name = (TextView) findViewById(R.id.txt_edit_name);
        nameprofile = (TextView) findViewById(R.id.profilename) ;
        email = (EditText) findViewById(R.id.txt_edit_mail);
        fab = (FloatingActionButton) findViewById(R.id.fabb);
         imge = (CircleImageView) findViewById(R.id.user_image);
        Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_anim);
        fab.startAnimation(animation);
    }

    public void loadInfoIntoViews(DataSnapshot dataSnapshot){
        email.setText(dataSnapshot.child("email").getValue().toString());
        name.setText(dataSnapshot.child("username").getValue().toString());
        nameprofile.setText(dataSnapshot.child("username").getValue().toString().substring(0,1).toUpperCase()
                +dataSnapshot.child("username").getValue().toString().substring(1));
        load.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        String imgURL = getIntent().getStringExtra("pic");
        if(imgURL!=null){
            Picasso.with(getApplicationContext()).load(imgURL).into(imge);
        }


    }
}
