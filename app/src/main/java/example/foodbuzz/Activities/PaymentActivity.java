package example.foodbuzz.Activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import example.foodbuzz.R;
import example.foodbuzz.data.DBCart;
import example.foodbuzz.data.Order;

public class PaymentActivity extends AppCompatActivity {
    Simplify simplify;
    CardEditor cardEditor;
    Button checkoutButton;
    ProgressDialog progressDialog;
    ImageView card,cash;
    TextView amount;
    boolean isCard;
    String format;
    DBCart cart;
    private FirebaseDatabase database;
    private DatabaseReference myRef2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        buildViews();
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        amount.setText(getIntent().getStringExtra("amount")+ " $");
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashClick();
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick();
            }
        });

        isCard = false;
        simplify = new Simplify();
        simplify.setApiKey("sbpb_ZGJjNmNjMTktNjBmMy00MjYyLTk4ZWMtY2VlMGY3ODk0MGQ4");
        checkoutButton.setEnabled(false);

        cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {
            @Override
            public void onStateChange(CardEditor cardEditor) {
                // isValid() == true : card editor contains valid and complete card information
                checkoutButton.setEnabled(cardEditor.isValid());
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PaymentActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getResources().getString(R.string.wait));
                progressDialog.show();
                payNow();
            }
        });
    }

    public void payNow() {
        if(checkoutButton.getText().toString().equals("PAY NOW")){
            simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback() {
                @Override
                public void onSuccess(CardToken cardToken) {
                    // ...
                    String token =  cardToken.getId();
                    String[] params = new String[2];
                    params[0] = token;
                    params[1] = "100";
                    new BackgroundThread().execute(params);

                }

                @Override
                public void onError(Throwable throwable) {
                    progressDialog.dismiss();
                    Toast.makeText(PaymentActivity.this, R.string.error6 +" : "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    // ...
                }
            });
        } else {
            Order order = new Order(getIntent().getStringExtra("name"),getIntent().getStringExtra("amount"),
                    getIntent().getStringExtra("thumb"),"Cash");
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            format = s.format(new Date());
            myRef2.child("orders").child(format).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                        String restoredText = prefs.getString("email", null);
                        myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                                        String restoredText = prefs.getString("email", null);
                                        if(dataSnapshot.getValue().toString().equals("null")){
                                            myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks").setValue(format);
                                        } else {
                                            myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks").setValue(dataSnapshot
                                                    .getValue().toString()+","+format);
                                        }
                                        progressDialog.dismiss();
                                        Toast.makeText(PaymentActivity.this, getResources().getString(R.string.orderok),
                                                Toast.LENGTH_SHORT).show();
                                        cart = new DBCart(getApplicationContext());
                                        cart.deleteOrder();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            });
        }

    }

    public void buildViews(){
        cardEditor = (CardEditor) findViewById(R.id.card_editor);
        checkoutButton = (Button) findViewById(R.id.paynow);
        card = (ImageView) findViewById(R.id.cardImage);
        cash = (ImageView) findViewById(R.id.cashImage);
        amount = (TextView) findViewById(R.id.amount);

    }

    public void cashClick(){
        checkoutButton.setText(getResources().getString(R.string.order));
        cardEditor.setVisibility(View.GONE);
        checkoutButton.setVisibility(View.VISIBLE);
        checkoutButton.setEnabled(true);

    }

    public void cardClick(){
        checkoutButton.setText(getResources().getString(R.string.pay_now));
        cardEditor.setVisibility(View.VISIBLE);
        checkoutButton.setVisibility(View.VISIBLE);
        isCard=true;
    }
    public class BackgroundThread extends AsyncTask<String,Void,Void>{



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            format = s.format(new Date());
            Order order;
            if(isCard){
                order = new Order(getIntent().getStringExtra("name"),getIntent().getStringExtra("amount"),
                        getIntent().getStringExtra("thumb"),"Credit Cart");
            } else {
                order = new Order(getIntent().getStringExtra("name"),getIntent().getStringExtra("amount"),
                        getIntent().getStringExtra("thumb"),"Cash");
            }
            myRef2.child("orders").child(format).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                        String restoredText = prefs.getString("email", null);
                        myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                                        String restoredText = prefs.getString("email", null);
                                        if(dataSnapshot.getValue().toString().equals("null")){
                                            myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks").setValue(format);
                                        } else {
                                            myRef2.child("users").child(restoredText.replace(".","")).child("historyLinks").setValue(dataSnapshot
                                                    .getValue().toString()+","+format);
                                        }
                                        progressDialog.dismiss();
                                        Toast.makeText(PaymentActivity.this, getResources().getString(R.string.paymentok),
                                                Toast.LENGTH_SHORT).show();
                                        cart = new DBCart(getApplicationContext());
                                        cart.deleteOrder();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            });

        }

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;
            HttpURLConnection con = null;
            try {
                 url = new URL("http://foodbuzz.herokuapp.com//charge.php");
                 con = (HttpURLConnection) url.openConnection();
                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String urlParameters = "simplifyToken="+params[0]+"&amount="+params[1];

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //print result
                System.out.println(response.toString());
                //
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }
    }
}
