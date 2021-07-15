package com.example.adminapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminapp.Admin.AdminDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText inputEmail,inputPassword;
    Button loginbtn;

    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail=findViewById(R.id.adminid);
        inputPassword=findViewById(R.id.adminpass);

        getSupportActionBar().hide();

        mLoadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();


        //check User is null or not
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,Dashboard.class);
            startActivity(intent);
            finish();
        }


        loginbtn= (Button) findViewById(R.id.btnlogin);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attemptlogin();
            }
            private void Attemptlogin() {
                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();

                //Validation
                if (email.isEmpty() || !email.contains("@"))
                {
                    inputEmail.setError("Email is not valid");
                }else if (password.isEmpty() || password.length()<=5)
                {
                    inputPassword.setError("Password is Invalid");
                }
                else
                {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait, while your credentials are verified");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    //Sign In
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                mLoadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this, AdminDetails.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                mLoadingBar.dismiss();
                                Toast.makeText(LoginActivity.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
