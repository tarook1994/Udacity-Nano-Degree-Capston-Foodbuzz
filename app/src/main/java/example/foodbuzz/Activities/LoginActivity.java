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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import example.foodbuzz.R;
import example.foodbuzz.data.User;

public class LoginActivity extends AppCompatActivity {
    Button login,btnFacebook;
    TextView register;
    EditText email,password;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback;
    LoginButton loginButton;
    View c;
    String emailFace="";
    private FirebaseDatabase database;
    private DatabaseReference myRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        myRef2 = database.getReference();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.d("fe eh","hena");
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                String emailFacebook="";
                                // Application code
                                try {

                                    emailFacebook = object.getString("email");
                                    emailFace = emailFacebook;
                                    Log.d("email",emailFacebook);
                                   String birthday = object.getString("picture"); // 01/31/1980 format

                                } catch (Exception e){
                                    Toast.makeText(LoginActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                Profile profile = Profile.getCurrentProfile();
                                if(profile!=null){
                                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                    editor.putString("email",emailFacebook);
                                    editor.apply();
                                    User user = new User(profile.getFirstName(),emailFacebook,"null","null");
                                    myRef2.child("users").child(emailFacebook.replace(".","")).setValue(user);
                                    Log.d("Taroo2","not null");
                                    nextActivity(profile);
                                } else {
                                    final String newEmail = emailFacebook;
                                    profileTracker = new ProfileTracker() {
                                        @Override
                                        protected void onCurrentProfileChanged(Profile profile, Profile newProfile) {
                                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                            editor.putString("email",newEmail);
                                            editor.apply();
                                            User user = new User(profile.getFirstName(),newEmail,"null","null");
                                            myRef2.child("users").child(newEmail).setValue(user);
                                            profileTracker.stopTracking();
                                            Log.d("Taroo2","null");
                                            nextActivity(newProfile);

                                        }
                                    };
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();




            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, callback);
        btnFacebook = (Button) findViewById(R.id.btn_facebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.callOnClick();

            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent main = new Intent(LoginActivity.this, Main2Activity.class);
            main.putExtra("Fbname", profile.getFirstName());
            main.putExtra("Fbsurname", profile.getLastName());
            main.putExtra("FbimageUrl", profile.getProfilePictureUri(200, 200).toString());
            main.putExtra("userID", profile.getId());
            main.putExtra("email",emailFace);
            Log.d("userID log", profile.getId());
            startActivity(main);
            finish();
        } else {
            Log.d("Taroo22","null");
        }
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
