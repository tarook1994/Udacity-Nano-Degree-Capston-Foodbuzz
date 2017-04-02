package example.foodbuzz.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import example.foodbuzz.R;

public class PaymentActivity extends AppCompatActivity {
    Simplify simplify;
     CardEditor cardEditor;
    Button checkoutButton;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cardEditor = (CardEditor) findViewById(R.id.card_editor);
        checkoutButton = (Button) findViewById(R.id.paynow);
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
    }
    public class BackgroundThread extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

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
