package ht.queeny.taxirapide2;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView textView_register;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Parse.initialize(this);
        progressDialog = new ProgressDialog(LoginActivity.this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        textView_register = (TextView) findViewById(R.id.register_textview);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                progressDialog.setMessage("Please Wait");
//                progressDialog.setTitle("Logging in");
//                progressDialog.show();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            parseLogin();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                ParseUser.logInInBackground("Email", "Password", new LogInCallback() {
//                    @Override
//                    public void done(ParseUser parseUser, ParseException e) {
//                        if (parseUser != null) {
//                            //Login Successful
//                            //you can display sth or do sth
//                            //For example Welcome + ParseUser.getUsername()
//                            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            //Login Fail
//                            //get error by calling e.getMessage()
//                            Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });

        textView_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    // The user has not logged in
                }else{
                    // The user has logged in
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();

                if(email.isEmpty() || pass.isEmpty()){
                    mEmailView.setError("Is empty");
                    return;
                }

                if(pass.isEmpty()){
                    mPasswordView.setError("Is empty");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Sign In", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(LoginActivity.this, "Not Sign In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    void parseLogin() {
        ParseUser.logInInBackground(mEmailView.getText().toString(), mPasswordView.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //alertDisplayer("Login Successful","Welcome "+parseUser.getUsername());
                } else {
                    progressDialog.dismiss();
                    alertDisplayer("Login Failed", e.getMessage()+" Please Try Again");
                }
            }
        });
    }

    void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }


}