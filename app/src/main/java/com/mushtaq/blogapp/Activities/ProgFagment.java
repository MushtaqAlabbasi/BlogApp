package com.mushtaq.blogapp.Activities;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mushtaq.blogapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgFagment extends Fragment {

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    ArrayList<Post> postList;



    public ProgFagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

             // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_prog_fagment, container, false);
        postRecyclerView  = fragmentView.findViewById(R.id.post_container);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);


        fillData();

        return fragmentView ;

    }


    //-------------------------------------


    void fillData(){

        postList =new ArrayList<>();


        StringRequest postRequest=new StringRequest(ServerApp.GET_PROG_CAT,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray postJA=new JSONArray(response);

                    for(int i=0;i<postJA.length();i++){
                        JSONObject JOpost=postJA.getJSONObject(i);
                        final Post post= new Post();
                        post.setUserPhoto(JOpost.getString("u_photo"));
                        post.setUserName(JOpost.getString("u_name"));
                        post.setContent(JOpost.getString("p_content"));
//                        post.setCategory(JOpost.getString("p_category"));
                        post.setTimeStamp(JOpost.getString("p_time"));

                        postList.add(post);
                    }

                    postAdapter=new PostAdapter(getContext(),postList);
                    postRecyclerView.setAdapter(postAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "unable to read data contact programmer", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue postQueue= Volley.newRequestQueue(getContext());

        postQueue.add(postRequest);

    }

}
