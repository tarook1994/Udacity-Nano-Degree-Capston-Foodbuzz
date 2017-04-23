package example.foodbuzz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import example.foodbuzz.R;
import example.foodbuzz.data.User;

public class RegisterActivity extends AppCompatActivity {
    String username,password,passwordConfirm,number,mail;
    EditText email,name,pass,passc,phone;
    Button reg;
    TextView goLogin;
    private FirebaseAuth mAuth;
    private  FirebaseDatabase database;
    private  DatabaseReference myRef2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        buildViews();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClick();
            }
        });

    }
    public void buildViews(){
        email = (EditText) findViewById(R.id.emailr);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.passwordr);
        passc = (EditText) findViewById(R.id.passwordrc);
        phone = (EditText) findViewById(R.id.phone);
        reg = (Button) findViewById(R.id.rreg);
        goLogin = (TextView) findViewById(R.id.loginpage);

    }
    public void onRegisterClick(){
        if(name.getText().toString().isEmpty()){

        } else {
            if(email.getText().toString().isEmpty()){

            } else {
                if(pass.getText().toString().isEmpty()){

                } else {
                    if(passc.getText().toString().isEmpty()){

                    } else {
                        if(phone.getText().toString().isEmpty()){

                        } else {
                             username = name.getText().toString();
                             password = pass.getText().toString();
                             passwordConfirm = passc.getText().toString();
                             number = phone.getText().toString();
                             mail = email.getText().toString();
                            if(password.equals(passwordConfirm)){
                                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage(getResources().getString(R.string.registering));
                                progressDialog.show();
                                mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                            editor.putString("email",email.getText().toString());
                                            editor.apply();
                                            User user = new User(username,mail,"null",number);
                                            String mailWithNoDot = mail.replace(".","");
                                            myRef2.child("users").child(mailWithNoDot).setValue(user);
                                            mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Intent i = new Intent(RegisterActivity.this, Main2Activity.class);
                                                        startActivity(i);
                                                        finish();
                                                     } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginfailed), Toast.LENGTH_SHORT).show();

                                                  }
                                                }
                                            });
                                        } else {

                                        }
                                    }
                                });
                            } else {
                                passc.setError(getResources().getString(R.string.passerror));
                            }
                        }
                    }
                }
            }
        }

    }

}
