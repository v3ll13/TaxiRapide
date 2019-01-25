package ht.queeny.taxirapide2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText editText_password, editText_email;
    Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Parse.initialize(this);

        // Set up the login form.
        editText_email = (EditText) findViewById(R.id.register_email);
        editText_password = (EditText) findViewById(R.id.register_password);
        registerButton = (Button) findViewById(R.id.email_sign_in_button);

//        registerButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParseUser user = new ParseUser();
//                user.setUsername(editText_email.getText().toString());
//                user.setPassword(editText_password.getText().toString());
//                user.signUpInBackground(new SignUpCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            //Register Successful
//                            //you can display sth or do sth
//                            Toast.makeText(RegisterActivity.this, "Register", Toast.LENGTH_SHORT).show();
//                        } else {
//                            //Register Fail
//                            //get error by calling e.getMessage()
//                            Toast.makeText(RegisterActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    // The user has not logged in
                }else{
                    // The user has logged in
                }
            }
        };


        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_email.getText().toString();
                String pass = editText_password.getText().toString();

                if(email.isEmpty() || pass.isEmpty()){
                    editText_email.setError("Is empty");
                    return;
                }

                if(pass.isEmpty()){
                    editText_password.setError("Is empty");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(RegisterActivity.this, "Not Sign Up", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


}

