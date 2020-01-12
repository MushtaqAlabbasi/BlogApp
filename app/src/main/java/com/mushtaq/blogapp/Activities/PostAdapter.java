package com.mushtaq.blogapp.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.mushtaq.blogapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Post> postlist;
    int positionToDdelete;


    public PostAdapter(Context mContext, ArrayList<Post> postlist) {
        this.mContext = mContext;
        this.postlist = postlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.post_template, parent, false);

        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.userNameView.setText(postlist.get(position).getUserName());
        holder.p_timeView.setText(postlist.get(position).getTimeStamp());
        holder.p_contentView.setText(postlist.get(position).getContent());

        Picasso.with(mContext).load(postlist.get(position).getUserPhoto())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public void removeItem(int position) {
        postlist.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }


    public void setPosts(ArrayList<Post> posts) {
        postlist = posts;
    }


    //-------------------------------------------------------------------------

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userNameView, p_timeView, p_contentView;
        ImageView userImage;


        public MyViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_photo_template);
            userNameView = itemView.findViewById(R.id.user_name_template);
            p_timeView = itemView.findViewById(R.id.p_time_template);
            p_contentView = itemView.findViewById(R.id.p_content_template);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();
                    positionToDdelete = getAdapterPosition();


                    postDetailActivity.putExtra("pID", postlist.get(position).getpID());
                    postDetailActivity.putExtra("uName", postlist.get(position).getUserName());
                    postDetailActivity.putExtra("pContent", postlist.get(position).getContent());
                    postDetailActivity.putExtra("postCategory", postlist.get(position).getCategory());
                    postDetailActivity.putExtra("postDate", postlist.get(position).getTimeStamp());
                    postDetailActivity.putExtra("userPhoto",postlist.get(position).getUserPhoto());

                    mContext.startActivity(postDetailActivity);

                }
            });

        }
    }

}








