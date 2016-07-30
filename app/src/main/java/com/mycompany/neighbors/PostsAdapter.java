package com.mycompany.neighbors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by joshua on 7/26/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private ArrayList<SinglePost> posts = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvUserName;
        public TextView tvStatus;


        public ViewHolder(View itemView) {
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUN);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);


        }
    }

    public PostsAdapter(ArrayList<SinglePost> p){

        posts = p;

    }

    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_feed_item,parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PostsAdapter.ViewHolder holder, int position) {

        holder.tvUserName.setText(posts.get(position).getUserName());
        holder.tvStatus.setText(posts.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}


