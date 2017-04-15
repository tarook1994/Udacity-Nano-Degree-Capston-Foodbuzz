package example.foodbuzz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import de.hdodenhof.circleimageview.CircleImageView;
import example.foodbuzz.R;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView register;
    EditText email,password;
    private FirebaseAuth mAuth;
    View c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getString("firstTime","").equals("") ){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("firstTime","yes");
            editor.apply();
            Log.d("shared",preferences.getString("firstTime",""));
            Intent i = new Intent(LoginActivity.this,GoogleCardsActivity.class);
            startActivity(i);
        }
        mAuth = FirebaseAuth.getInstance();
        buildViews();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = v;
                signInAction(v);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public void buildViews(){
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.reg);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }



    public  void signInAction(View view){
        if(email.getText().toString().isEmpty()){
            email.setError(Html.fromHtml(getResources().getString(R.string.error1)));

        } else {
            if(password.getText().toString().isEmpty()){
                password.setError(Html.fromHtml(getResources().getString(R.string.error2)));
            } else {
                if(!email.getText().toString().contains("@")){
                    email.setError(getResources().getString(R.string.error3));
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getResources().getString(R.string.wait));
                    progressDialog.show();
                    String mail = email.getText().toString();
                    String pass = password.getText().toString();
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                editor.putString("email",email.getText().toString());
                                editor.apply();
                                Intent i = new Intent(LoginActivity.this,Main2Activity.class);
                                i.putExtra("email",email.getText().toString());
                                startActivity(i);
                            } else {
                                progressDialog.dismiss();
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    Snackbar.make(c, getResources().getString(R.string.error4), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    Snackbar.make(c, getResources().getString(R.string.error5), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

            }
        }

    }
}
