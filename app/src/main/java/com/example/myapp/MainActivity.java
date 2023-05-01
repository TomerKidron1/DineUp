package com.example.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Signinemail,Signinpass;
    TextView forgot,registertext;
    Button signinNext,visiblepass;
    ImageView google,table;
    FirebaseAuth uAuth;
    GoogleSignInClient googleSignInClient;
    NetReciever netReciever = new NetReciever();
    RadioGroup radio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signin_test);
        Signinemail = findViewById(R.id.signin_email2);
        Signinpass = findViewById(R.id.signin_password1);
        forgot = findViewById(R.id.forgotpassword);
        radio = findViewById(R.id.radio_signin_screen);
        signinNext = findViewById(R.id.login_button);
        google = findViewById(R.id.google_icon);
        registertext = findViewById(R.id.register_radio_text);
        registertext.setOnClickListener(this);
        visiblepass = findViewById(R.id.visible_button);
        table = findViewById(R.id.table);
        Utils.delay(1, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shaking);
                animation.setDuration(7000);
                table.startAnimation(animation);
            }
        });
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("417135447197-dv8c0cip54omah3p9kc2j2vvoudbvaf4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
                ,googleSignInOptions);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });
        forgot.setOnClickListener(this);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.signin_radio_reg){
                    Intent intent = new Intent(MainActivity.this, Register.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        uAuth = FirebaseAuth.getInstance();
        signinNext.setOnClickListener(this);
        visiblepass.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            signInAccountTask.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                @Override
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    Toast.makeText(MainActivity.this,"Google Sign in Successful",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("tag", "createUserWithEmail:failure", signInAccountTask.getException());

                }
            });


            if(signInAccountTask.isSuccessful())
            {
                Toast.makeText(MainActivity.this,"Google Sign in Successful",Toast.LENGTH_LONG).show();
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        uAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                                            SharedPreferences.Editor spe = sp.edit();
                                            spe.putString("email", user.getEmail());
                                            spe.putString("name", user.getDisplayName());
                                            spe.commit();
                                            startActivity(new Intent(MainActivity.this,MainScreen.class));
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == forgot){
            if(Signinemail.getText().toString().equals("")||Signinemail.getText().toString()==null){
                Toast.makeText(MainActivity.this,"enter email in the box above",Toast.LENGTH_LONG).show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Did you forgot your password?");
                builder.setMessage("This will send you an email with instructions on how to reset your password");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uAuth.sendPasswordResetEmail(Signinemail.getText().toString());
                        Toast.makeText(MainActivity.this,"an email has been sent to you with instructions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }
        if(view == signinNext){
            if(!Signinpass.getText().toString().equals("")&& !Signinemail.getText().toString().equals("")) {
                uAuth.signInWithEmailAndPassword(Signinemail.getText().toString().trim(), Signinpass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putString("email", Signinemail.getText().toString());
                            spe.putString("password", Signinpass.getText().toString());
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            spe.putString("name",user.getDisplayName());
                            spe.commit();
                            startActivity(new Intent(MainActivity.this, MainScreen.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
                Toast.makeText(MainActivity.this,"Enter all Parameters",Toast.LENGTH_LONG).show();
        }
        if(view == visiblepass){
                if(Signinpass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    Signinpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    visiblepass.setBackgroundResource(R.drawable.eyeslash);
                }
                else{
                    Signinpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    visiblepass.setBackgroundResource(R.drawable.eye_icon);

                }
                Signinpass.setSelection(Signinpass.length());

        }
    }


    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReciever,intentFilter);
        super.onStart();

    }

    @Override
    protected void onStop() {
        unregisterReceiver(netReciever);
        super.onStop();
    }
}