package com.mushtaq.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int PickPhotoRC = 1;
    Uri pickedImgUri;

    boolean correctEmail, matchedPassword;

    private EditText userEmail, userPassword, confirmPass, userName;
    private ProgressBar loadingProgress;
    private Button regBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ini views
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        confirmPass = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        regBtn = findViewById(R.id.regBtn);

        loadingProgress = findViewById(R.id.regProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);


        userEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0 && userEmail.length() > 0) {

                    CharSequence email = userEmail.getText().toString();

                    if (!isValidEmail(email)) {
                        userEmail.requestFocus();
                        userEmail.setError("Enter Correct Email");
                        correctEmail = false;
                    } else
                        correctEmail = true;

                }
            }
        });


        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() > 0 && userPassword.length() > 0) {
                    if (!(userPassword.getText().toString().equals(confirmPass.getText().toString()))) {
                        // give an error that password and confirm password not match
                        confirmPass.setError("confirm password does not match password");
                        matchedPassword = false;
                    } else
                        matchedPassword = true;
                }

            }

        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);

                loadingProgress.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = confirmPass.getText().toString();
                final String name = userName.getText().toString();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty()) {

                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                } else {

                    if (!matchedPassword) {
                        showMessage("password should be matched");
                        regBtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                        return;
                    }

                    if (!correctEmail) {
                        showMessage("you have to enter valid email");
                        regBtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);
                        return;
                    }

                    // everything is ok and all fields are filled now we can start creating user account
                    // CreateUserAccount method will try to create the user if the email is valid
                    CreateUserAccount(email, name, password);
                }
            }


        });


//        enableRegBTN(correctEmail,matchedPassword);


        ImgUserPhoto = findViewById(R.id.regUserPhoto);

//        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Build.VERSION.SDK_INT >= 22) {
//
//                    checkAndRequestForPermission();
//
//                } else {
//                    openGallery();
//                }
//
//            }
//        });


    }//end of onCreate


    private void CreateUserAccount(final String email, final String name, final String password) {


        StringRequest request = new StringRequest(Request.Method.POST,
                ServerApp.ADD_USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                regBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

                try {
                    JSONObject result = new JSONObject(response);
                    showMessage(result.getString("message"));

                    if (result.getString("success").equals("1")) {
                        Intent HomeActivity = new Intent
                                (RegisterActivity.this, com.mushtaq.blogapp.Activities.LoginActivity.class);
                        HomeActivity.putExtra("justSignUp", true);
                        startActivity(HomeActivity);
                    } else if (result.getString("success").equals("2")) {
                        userEmail.setError("this email is used before");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                parameter.put("u_email", email);
                return parameter;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(request);

    }


//    private void updateUI() {

//        Intent homeActivity = new Intent(getApplicationContext(), Home.class);
//        startActivity(homeActivity);
//        finish();
//
//
//    }

    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

//    private void openGallery() {
//        //TODO: open gallery intent and wait for user to pick an image !
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, PickPhotoRC);
//    }

//    private void checkAndRequestForPermission() {
//
//
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
//
//            } else {
//                ActivityCompat.requestPermissions(RegisterActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
//            }
//
//        } else
//            openGallery();
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == PickPhotoRC && data != null) {
//
//            // the user has successfully picked an image
//            // we need to save its reference to a Uri variable
//            pickedImgUri = data.getData();
//            ImgUserPhoto.setImageURI(pickedImgUri);
//
//        }
//
//
//    }
//

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}



