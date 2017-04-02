package example.foodbuzz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.CardToken;
import com.simplify.android.sdk.Simplify;

import example.foodbuzz.R;

public class PaymentActivity extends AppCompatActivity {
    Simplify simplify;
     CardEditor cardEditor;
    Button checkoutButton;



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
                payNow();
            }
        });
    }

    public void payNow() {
        simplify.createCardToken(cardEditor.getCard(), new CardToken.Callback() {
            @Override
            public void onSuccess(CardToken cardToken) {
                // ...
            }

            @Override
            public void onError(Throwable throwable) {
                // ...
            }
        });
    }
}
