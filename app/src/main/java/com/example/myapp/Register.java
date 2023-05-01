package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText registName,registEmail,registPass;
    Button registNext,passvisible;
    ImageView google_icon,table;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myref;
    TextView signintext;
    GoogleSignInClient googleSignInClient;
    RadioGroup radio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_test);
        registName = findViewById(R.id.register_name);
        registEmail = findViewById(R.id.register_email);
        registPass = findViewById(R.id.register_password);
        registNext = findViewById(R.id.register_button);
        passvisible = findViewById(R.id.visible_button_regist);
        google_icon = findViewById(R.id.google_icon2);
        radio = findViewById(R.id.radio_register);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.register_radio_sign){
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        signintext = findViewById(R.id.signin_radio_text2);
        database = FirebaseDatabase.getInstance();
        myref = database.getReference("Authentication");
        signintext.setOnClickListener(this);
        table = findViewById(R.id.table2);
        Utils.delay(1, new Utils.DelayCallback() {
            @Override
            public void afterDelay() {
                Animation animation = AnimationUtils.loadAnimation(Register.this,R.anim.shaking);
                animation.setDuration(7000);
                table.startAnimation(animation);
            }
        });
        passvisible.setOnClickListener(this);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("417135447197-dv8c0cip54omah3p9kc2j2vvoudbvaf4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(Register.this
                ,googleSignInOptions);
        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        registNext.setOnClickListener(this);
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
                    Toast.makeText(Register.this,"Google Sign in Successful",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("tag", "createUserWithEmail:failure", signInAccountTask.getException());

                }
            });


            if(signInAccountTask.isSuccessful())
            {
                Toast.makeText(Register.this,"Google Sign in Successful",Toast.LENGTH_LONG).show();
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        mAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                                            SharedPreferences.Editor spe = sp.edit();
                                            spe.putString("email",user.getEmail());
                                            spe.putString("name", user.getDisplayName());
                                            spe.commit();


                                            startActivity(new Intent(Register.this,MainScreen.class));
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                        }
                                        else
                                        {
                                            Toast.makeText(Register.this,"Authentication Failed",Toast.LENGTH_LONG).show();
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
        if(view == passvisible){
            if(registPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                registPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passvisible.setBackgroundResource(R.drawable.eyeslash);
            }
            else{
                registPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passvisible.setBackgroundResource(R.drawable.eye_icon);

            }
            registPass.setSelection(registPass.length());

        }
        if(view == registNext){
            if(!checkCharacters(registEmail.getText().toString(),'@')||!checkCharacters(registEmail.getText().toString(),'.'))
               Toast.makeText(Register.this,"email incorrect", Toast.LENGTH_LONG).show();
            else if(registPass.getText().toString()!=null&&registEmail.getText().toString()!=null){
                mAuth.createUserWithEmailAndPassword(registEmail.getText().toString().trim(), registPass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Signing up successful.",
                                            Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                                    SharedPreferences.Editor spe = sp.edit();
                                    spe.putString("email", registEmail.getText().toString());
                                    spe.putString("password",registPass.getText().toString());
                                    spe.putString("name",registName.getText().toString());
                                    int iend = registEmail.getText().toString().indexOf("@");
                                    String username="";
                                    if(iend!=-1)
                                        username=registEmail.getText().toString().substring(0,iend);
                                    String finalUsername = username;
                                    myref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Map<String,String> name = new HashMap<>();
                                            name.put("name",registName.getText().toString());
                                            myref.child(finalUsername).setValue(name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                    spe.commit();
                                    Intent intent = new Intent(Register.this, MainScreen.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                } else {

                                    Toast.makeText(Register.this, "Signing up failed.",
                                            Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Register.this,"this email already exist", Toast.LENGTH_LONG).show();
                                }
                            }
                        });



            }
        }
        if(view == passvisible){
            if(registPass.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD){
                registPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            else{
                registPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            registPass.setSelection(registPass.length());

        }

    }
    public boolean checkCharacters(String string, char Char){
        for(int i=0;i<string.length();i++){
            if(string.charAt(i)==Char)
                return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
