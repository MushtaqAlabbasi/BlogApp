package com.mushtaq.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mushtaq.blogapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText userMail, userPassword;
    private Button btnLogin, btnSignup;
    private ProgressBar loginProgress;
    private ImageView loginPhoto;
    UserSessionManager session;

    boolean justSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        btnSignup = findViewById(R.id.signUp);
        loginProgress = findViewById(R.id.login_progress);
        loginPhoto = findViewById(R.id.login_photo);

        //----------------------------------------------------

        checkIfJustsignUp();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();

            }
        });


        loginProgress.setVisibility(View.INVISIBLE);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String name = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if (name.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify All Field");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } else {
                    signIn(name, password);
                }

            }
        });


    }


    private void signIn(final String name, final String password) {

        StringRequest request = new StringRequest(Request.Method.POST,
                ServerApp.GET_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (response.equals("error name or password")) {
                        showMessage(response);
                        btnLogin.setVisibility(View.VISIBLE);
                        loginProgress.setVisibility(View.INVISIBLE);
                    } else {

                        JSONObject data = new JSONObject(response);

                         String u_id = data.getString("u_id");
                         String u_name = data.getString("u_name");
                         String u_email = data.getString("u_email");
                         String u_photo = data.getString("u_photo");

                        session.createUserLoginSession(u_id,u_name,u_email,u_photo);

                        btnLogin.setVisibility(View.INVISIBLE);
                        loginProgress.setVisibility(View.VISIBLE);

                        updateUI();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                    showMessage(response);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                parameter.put("u_name", name);
                parameter.put("u_pass", password);
                return parameter;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);

    }


    private void updateUI() {

        Intent HomeActivityIntent = new Intent(this, com.mushtaq.blogapp.Activities.Home.class);
        startActivity(HomeActivityIntent);
        finish();

    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    private void checkIfJustsignUp() {
        justSignUp = getIntent().getBooleanExtra("justSignUp", false);
        if (justSignUp)
            btnSignup.setVisibility(View.INVISIBLE);
        else
            btnSignup.setVisibility(View.VISIBLE);
    }
}
