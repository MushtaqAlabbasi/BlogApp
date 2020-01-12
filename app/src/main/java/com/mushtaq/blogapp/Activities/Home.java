package com.mushtaq.blogapp.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mushtaq.blogapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mushtaq.blogapp.Activities.RegisterActivity.PReqCode;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public interface FragmentRefreshListener{
        void onRefresh();
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;



    private final int GALLERY = 1;
    JSONObject jsonObject;
    RequestQueue rQueue;
    UserSessionManager session;

    //----------------

    Dialog popAddPost;
    ImageView popupUserImage;
    Spinner popupCat;
    EditText popupDescription;
    Button popupAddBtn;
    ProgressBar popupClickProgress;
    String choosenCat;


//    String uID, uName;


    ArrayList<String> categories = new ArrayList<>();

//--------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new UserSessionManager(getApplicationContext());

        if(session.checkLogin())
            finish();



        popupCat = findViewById(R.id.popup_category);


        // ini popup
        iniPopup();

        getCategory();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popAddPost.show();

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();


        // set the home fragment as the default one
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //----------------------------------------------------------------

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        } else if (id == R.id.nav_prog) {
            getSupportActionBar().setTitle("Programing");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProgFagment()).commit();

        } else if (id == R.id.nav_des) {
            getSupportActionBar().setTitle("Designing");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new DesFragment()).commit();

        } else if (id == R.id.nav_eng) {
            getSupportActionBar().setTitle("Programing");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new EngFragment()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            session.logoutUser();
//            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(loginActivity);
//            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------------------------------------

    public void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.nav_username);

        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);

        navUserMail.setText(CurrentUserSession.getUEMAIL());
        navUsername.setText(CurrentUserSession.getUNAME());

//        Picasso.with(Home.this).load(CurrentUserSession.getUPHOTO()).into(navUserPhot);


        Picasso.with(Home.this).load(CurrentUserSession.getUPHOTO())
                .memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(navUserPhot);

        navUserPhot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkAndRequestForPermission();

            }
        });


    }


    //---------------------------------------------------------

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupCat = popAddPost.findViewById(R.id.popup_category);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!choosenCat.isEmpty() && !popupDescription.getText().toString().isEmpty()) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to database
                    // create post Object
                    Post post = new Post(choosenCat, popupDescription.getText().toString(), CurrentUserSession.getUID());

                    // Add post to database

                    addPost(post);

                } else {
                    showMessage("Please verify all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }

            }

        });

    }

    //-------------------------------------------------------------------
    void getCategory() {

        categories.add("Programming");
        categories.add("Designing");
        categories.add("Engineering");
        categories.add("Select a category");

        popupCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected = adapterView.getItemAtPosition(i).toString();
                if (selected.equals("Programming"))
                    choosenCat = "1";
                else if (selected.equals("Designing"))
                    choosenCat = "2";
                else if (selected.equals("Engineering"))
                    choosenCat = "3";
                else
                    choosenCat = "";


//                Toast.makeText(Home.this, choosenCat+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spenner,
                categories) {
            @Override
            public int getCount() {
                // to show hint
                return 3;
            }
        };

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        popupCat.setAdapter(dataAdapter);
        popupCat.setSelection(3);

    }

    //-----------------------------------------------

    private void addPost(final Post post) {

        StringRequest request = new StringRequest(Request.Method.POST,
                ServerApp.ADD_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

//                    JSONObject data=new JSONObject(response);
                    showMessage(response);

                    if (response.equals("inserted successfully")) {
                        popupClickProgress.setVisibility(View.INVISIBLE);
                        popupAddBtn.setVisibility(View.VISIBLE);
                        popAddPost.dismiss();
                        getFragmentRefreshListener().onRefresh();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    showMessage(response);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showMessage(error.getMessage());
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<>();
                parameter.put("p_category", post.getCategory());
                parameter.put("p_content", post.getContent());
                parameter.put("u_id", post.getCurrentUser());
                return parameter;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Home.this);
        queue.add(request);

    }


    //-------------------------------------

    private void showMessage(String message) {

        Toast.makeText(Home.this, message, Toast.LENGTH_LONG).show();

    }

    //-------------------------------------

    private void uploadImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        try {
            jsonObject = new JSONObject();
            //String u_id= uID;
            jsonObject.put("u_id", CurrentUserSession.getUID());
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServerApp.UPLOAD_IMG_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
//                        Log.e("aaaaaaa", jsonObject.toString());

                        try {

                            showMessage(jsonObject.getString("message"));
                            if(jsonObject.getString("success").equals("1")){
                                updateNavHeader();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rQueue.getCache().clear();
//                        Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(Home.this);
        rQueue.add(jsonObjectRequest);

    }


    //----------------------------------


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Home.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            // everything goes well : we have permission to access user gallery
            openGallery();

    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);

//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent,GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    imageView.setImageBitmap(bitmap);
                    uploadImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        updateNavHeader();
//    }
}




