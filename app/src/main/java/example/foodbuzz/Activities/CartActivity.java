package example.foodbuzz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import example.foodbuzz.Adapters.CartAdapter;
import example.foodbuzz.Adapters.Divider;
import example.foodbuzz.Adapters.RestaurantAdapter;
import example.foodbuzz.R;
import example.foodbuzz.data.DBCart;
import example.foodbuzz.data.OrderItem;

public class CartActivity extends AppCompatActivity {

    TextView total;
    RecyclerView cartItems;
    Button checkout;
    DBCart cart;
    ArrayList<OrderItem> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cart = new DBCart(getApplicationContext());
        order = new ArrayList<>();
        buildViews();
        buildRecyclerView();

    }
    public void buildViews(){

        total = (TextView) findViewById(R.id.cartTotal);
        cartItems = (RecyclerView) findViewById(R.id.cartList);
        checkout = (Button) findViewById(R.id.checkout);
    }

    public void buildRecyclerView(){
        order = cart.getAllOrder();
        CartAdapter mAdapter = new CartAdapter(getApplication(),order);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cartItems.setLayoutManager(mLayoutManager);
        cartItems.setItemAnimator(new DefaultItemAnimator());
        cartItems.addItemDecoration(new Divider(getApplicationContext(), LinearLayoutManager.VERTICAL));
        cartItems.setAdapter(mAdapter);
        double itemFinalPrice=0.0;
        double totalPrice = 0.0;
        for(int i =0;i<order.size();i++){
            Log.d("debug1",order.get(i).getNumber());
            Log.d("debug2",order.get(i).getPrice().replace("$",""));
            itemFinalPrice= Integer.parseInt(order.get(i).getNumber())*Double.parseDouble(order.get(i).getPrice().replace("$",""));
            totalPrice += itemFinalPrice;
        }
        total.setText("Total Price : "+totalPrice);

    }
}
