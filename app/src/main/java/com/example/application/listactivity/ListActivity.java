package com.example.application.listactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.application.PostDetails.PosrtDetails;
import com.example.application.databinding.ActivityListBinding;
import com.example.application.models.pojos.Post;
import com.example.application.network.instance.RetrofitInstance;
import com.example.application.network.interfaces.PostCalls;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;
    private PostAdapter postAdapter;
    public ArrayList<Post> arrayList;
    public List<Post> postList;
    public Call<List<Post>> posts;
    public static PostCalls postCalls;
    private static final String TAG = "ListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postCalls = RetrofitInstance.getApiClient();

        fetchData();
    }

    private void fetchData() {
     posts = ListActivity.postCalls.getPosts();
     posts.clone().enqueue(new Callback<List<Post>>() {
         @Override
         public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
             if(response.isSuccessful()){
                 postList = response.body();
                 arrayList = (ArrayList<Post>) postList;
                 setAdapter();

             }
         }

         @Override
         public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
             Context context = getApplicationContext();
             CharSequence text = "Failed to load posts";
             int duration = Toast.LENGTH_SHORT;

             Toast toast = Toast.makeText(context, text, duration);
             toast.show();
         }
     });


    }

    private void setAdapter() {
        postAdapter = new PostAdapter(arrayList);
        binding.recycler.setAdapter(postAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        postAdapter.notifyDataSetChanged();
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                Log.d(TAG, "onItemClick: " + post.getUserId());
                Log.d(TAG, "onItemClick: " + post.getId());
                Intent intent = new Intent(ListActivity.this, PosrtDetails.class);
                intent.putExtra("id",post.getId());
                intent.putExtra("uid",post.getUserId());
                startActivity(intent);
            }
        });
    }
}