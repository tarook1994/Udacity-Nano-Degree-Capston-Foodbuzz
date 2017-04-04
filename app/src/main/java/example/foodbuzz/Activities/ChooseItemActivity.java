package example.foodbuzz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import example.foodbuzz.R;
import example.foodbuzz.data.DBCart;
import example.foodbuzz.data.OrderItem;

import static java.lang.Double.parseDouble;

public class ChooseItemActivity extends AppCompatActivity {
    Button addToCart,add,sub;
    TextView itemPrice,itemTitle,itemDesc,totalPrice,numberOfItems;
    ImageView icon;
    int currentCount;
    DBCart cart;
    double modifiedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);
        buildViews();
        modifiedPrice=0.0;
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("icon")).into(icon);
        currentCount =1;
        itemTitle.setText(getIntent().getStringExtra("title"));
        itemDesc.setText(getIntent().getStringExtra("desc"));
        itemPrice.setText(getResources().getText(R.string.price)+getIntent().getStringExtra("price"));
        totalPrice.setText(getResources().getText(R.string.total_price)+getIntent().getStringExtra("price"));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick();
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               subClick();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartClick();
            }
        });



    }
    public void buildViews(){
        addToCart = (Button)findViewById(R.id.add_to_cart);
        add = (Button) findViewById(R.id.addItem);
        sub = (Button) findViewById(R.id.sub);
        numberOfItems = (TextView) findViewById(R.id.inputNumber);
        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemTitle = (TextView) findViewById(R.id.itemTitle);
        itemDesc = (TextView) findViewById(R.id.itemDesc);
        totalPrice = (TextView) findViewById(R.id.totalItemPrice);
        icon = (ImageView) findViewById(R.id.itemImage);
    }

    public void addClick(){
        if(numberOfItems.getText().toString().isEmpty() || numberOfItems.getText().toString().equals("0")){
            numberOfItems.setText("1");
        } else {
            String current = numberOfItems.getText().toString();
            int counter = Integer.parseInt(current)+1;
            currentCount++;
            modifiedPrice = Double.parseDouble(getIntent().getStringExtra("price").replace("$",""))*currentCount;
            totalPrice.setText(getResources().getText(R.string.total_price)+Double.toString(modifiedPrice)+" $");
            numberOfItems.setText(counter+"");
        }
    }

    public void subClick(){
        if(numberOfItems.getText().toString().isEmpty() || numberOfItems.getText().toString().equals("1")){

        } else {
            if( !numberOfItems.getText().toString().equals("")){
                String current = numberOfItems.getText().toString();
                int counter = Integer.parseInt(current)-1;
                currentCount--;
                modifiedPrice = Double.parseDouble(getIntent().getStringExtra("price").replace("$",""))*currentCount;
                totalPrice.setText(getResources().getText(R.string.total_price)+Double.toString(modifiedPrice)+" $");
                numberOfItems.setText(counter+"");
            }

        }
    }

    public void addToCartClick(){
        cart = new DBCart(getApplicationContext());
        OrderItem orderItem = new OrderItem(1,getIntent().getStringExtra("title"),currentCount+"",modifiedPrice+"");
        cart.addRow(orderItem);
        finish();
    }
}
