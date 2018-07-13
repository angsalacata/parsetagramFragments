package com.example.angsala.parsetagram;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.angsala.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
  public final String TAG = "FeedActivityTAG";
  ArrayList<Post> mposts;
  PostAdapter postAdapter;
  RecyclerView rvViewPosts;
  private SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);
    mposts = new ArrayList<>();
    postAdapter = new PostAdapter(mposts);
    rvViewPosts = (RecyclerView) findViewById(R.id.rvPosts);
    rvViewPosts.setLayoutManager(new LinearLayoutManager(this));

    loadTopPosts();
    rvViewPosts.setAdapter(postAdapter);

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    swipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            swipeRefreshLayout.setRefreshing(false);
            fetchTimeline(0);
          }
        });
  }

  private void loadTopPosts() {
    final Post.Query postsQuery = new Post.Query();
    postsQuery.withUser().orderByAscending("createdAt");

    postsQuery.findInBackground(
        new FindCallback<Post>() {
          @Override
          public void done(List<Post> posts, ParseException e) {
            if (e == null) {
              for (int i = posts.size() - 1; i >= 0; i--) {
                Log.d(
                    TAG,
                    "Post number "
                        + i
                        + " description: "
                        + posts.get(i).getDescription()
                        + "\n username = "
                        + posts
                            .get(i)
                            .getUser()
                            .getUsername()); // user has been attached to the post)

                mposts.add(posts.get(i));
                postAdapter.notifyItemInserted(mposts.size() - 1);
              }

            } else {
              e.printStackTrace();
            }
          }
        });
  }

  public void fetchTimeline(int page) {
    postAdapter.clear();
    loadTopPosts();
    postAdapter.addAll(mposts);
  }
}
