package com.mushtaq.blogapp.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


//    String currentUser;

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    ArrayList<Post> postList;
    String pID;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        postRecyclerView = fragmentView.findViewById(R.id.post_container);

        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        postRecyclerView.setHasFixedSize(true);

        postRecyclerView.setItemAnimator(new DefaultItemAnimator());

//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(
//                0, ItemTouchHelper.LEFT, HomeFragment.this);


        fillPostRV();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // get adapter position

                final int position = viewHolder.getAdapterPosition();
                 pID = postList.get(position).getpID();

                if (!CurrentUserSession.getUID().equals(postList.get(position).getUserId())) {
                    postAdapter.notifyDataSetChanged();
                    showMessage("you R not allowed to delete this post");
                    return;
                } else {
                    confirmDialog(viewHolder);
                }

            }
        };


        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(postRecyclerView);




        ((Home)getActivity()).setFragmentRefreshListener(new Home.FragmentRefreshListener(){

            @Override
            public void onRefresh() {
                fillPostRV();
            }
        });


        return fragmentView;

    }//end of onCreate



    //-------------------------------------

    void fillPostRV() {

        postList = new ArrayList<>();

        StringRequest postRequest = new StringRequest(ServerApp.GET_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray postJA = new JSONArray(response);

                    for (int i = 0; i < postJA.length(); i++) {
                        JSONObject JOpost = postJA.getJSONObject(i);
                        Post post = Post.createPost(JOpost);
                        postList.add(post);
                    }

                    postAdapter = new PostAdapter(getContext(), postList);
                    postRecyclerView.setAdapter(postAdapter);
                    postAdapter.notifyDataSetChanged();


//                    updateUI();


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

        RequestQueue postQueue = Volley.newRequestQueue(getContext());

        postQueue.add(postRequest);

    }


    private void showMessage(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

    void deletePost(final String pid) {

        StringRequest request = new StringRequest(Request.Method.POST,
                ServerApp.DELET_POST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject result = new JSONObject(response);

                    showMessage(result.getString("message"));

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
                parameter.put("p_id", String.valueOf(pid));
                parameter.put("u_id", CurrentUserSession.getUID());
                return parameter;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }


    private void confirmDialog(final RecyclerView.ViewHolder viewHolder){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm to Delete!");
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setMessage("You are about to delete this post from database. Do you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePost(pID);
                // remove the item from recycler view
                postAdapter.removeItem(viewHolder.getAdapterPosition());
                postAdapter.notifyDataSetChanged();
//                Toast.makeText(HomeFragment.this, "You've choosen to delete all records", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postAdapter.notifyDataSetChanged();
            }
        });

        builder.show();

    }

}

