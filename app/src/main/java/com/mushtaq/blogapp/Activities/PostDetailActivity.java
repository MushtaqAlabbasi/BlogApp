package com.mushtaq.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mushtaq.blogapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imgUserPost,imgCurrentUser;
    TextView txtPostDesc,txtPostDateName,txtPostTitle,userNameView;
    EditText editTextComment;
    Button btnAddComment;
    String pID;
//    //currentUserId;

    CommentsAdapter commentAdapter;
    RecyclerView RvComment;
    ArrayList<CommentsModel> commentList;
//    static String COMMENT_KEY = "Comment" ;
//    String comment_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        pID=getIntent().getStringExtra("pID");
//        currentUserId =getIntent().getStringExtra("currentUserID");



        // let's set the statue bar to transparent
//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getSupportActionBar().hide();



        // ini Views


        RvComment = findViewById(R.id.rv_comment);

        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        userNameView = findViewById(R.id.post_detail_user_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);


        // add Comment button click listner

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editTextComment.getText().toString().isEmpty()) {

                    String comment_content = editTextComment.getText().toString();

                    //everything is okey no empty or null value
                    // TODO Create Comment Object and add it to database
                    // create post Object
                   CommentsModel comment = new CommentsModel(comment_content,CurrentUserSession.getUID(),pID);
                    // Add post to database
                    addComment(comment);

                }
                else
                {
                    showMessage("Please enter a comment") ;
                    btnAddComment.setVisibility(View.VISIBLE);
                }

            }
        });

        String postCategory = getIntent().getExtras().getString("postCategory");
        txtPostTitle.setText(postCategory);

        String pContent = getIntent().getExtras().getString("pContent");
        txtPostDesc.setText(pContent);

        String postDate = getIntent().getExtras().getString("postDate");
        txtPostDateName.setText(postDate);


        String uName = getIntent().getExtras().getString("uName");
        userNameView.setText(uName);

        Picasso.with(this).load(getIntent().getExtras().getString("userPhoto"))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgUserPost);


        // ini Recyclerview Comment
        fillCommentsRV();
    }


    //-------------------------------------------------------

    private void addComment(final CommentsModel comment) {

        StringRequest request=new StringRequest(Request.Method.POST,
                ServerApp.ADD_COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    showMessage( response );
                    if(response.equals("inserted successfully")){
                    fillCommentsRV();
                    editTextComment.getText().clear();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    showMessage( response );

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showMessage( error.getMessage());
            }


        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parameter=new HashMap<>();
                parameter.put("com_text",comment.getComText());
                parameter.put("u_id",comment.getuID());
                parameter.put("p_id",comment.getpID());
                return  parameter;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(PostDetailActivity.this);
        queue.add(request);

    }

    //-------------------------------------
    private void showMessage(String message) {

        Toast.makeText(PostDetailActivity.this,message,Toast.LENGTH_LONG).show();

    }
    //-------------------------------------

    private void fillCommentsRV() {

        RvComment.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this));
        RvComment.setHasFixedSize(true);

        commentList =new ArrayList<>();


        StringRequest postRequest=new StringRequest(Request.Method.POST,ServerApp.GET_COMMENTS_URL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray commentJA=new JSONArray(response);

                    for(int i=0;i<commentJA.length();i++){
                        JSONObject JOcomment=commentJA.getJSONObject(i);
                        commentList.add(CommentsModel.createComment(JOcomment));
                    }

                    commentAdapter=new CommentsAdapter(PostDetailActivity.this,commentList);
                    RvComment.setAdapter(commentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    showMessage("unable to read data contact programmer");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parameter=new HashMap<>();
                parameter.put("pID",pID);
                return  parameter;
            }
        };

        RequestQueue postQueue= Volley.newRequestQueue(PostDetailActivity.this);

        postQueue.add(postRequest);

    }


}