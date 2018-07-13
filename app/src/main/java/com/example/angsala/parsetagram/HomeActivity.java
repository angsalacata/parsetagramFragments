package com.example.angsala.parsetagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.angsala.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
  String TAG = "HomeActivity";
  private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_175243.jpg";
  private EditText inputDescription;
  private Button buttonRefresh;
  private Button buttonCreate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    ParseObject.registerSubclass(Post.class);

    inputDescription = (EditText) findViewById(R.id.inputDescription);
    buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
    buttonCreate = (Button) findViewById(R.id.fragmentButtonCreate);

    buttonRefresh.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            loadTopPosts();
          }
        });

    buttonCreate.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // instance objects for createPosts
            final String description = inputDescription.getText().toString();
            final ParseUser currentUser = ParseUser.getCurrentUser();

            final File file = new File(imagePath);
            final ParseFile picture = new ParseFile(file);
            createPosts(description, picture, currentUser);
          }
        });

    loadTopPosts();
  }

  private void createPosts(String description, ParseFile imageFile, ParseUser user) {
    Post newPost = new Post();
    newPost.setDescription(description);
    newPost.setImage(imageFile);
    newPost.setUser(user);

    newPost.saveInBackground(
        new SaveCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.d(TAG, "Successfully posted new post");
            } else {
              e.printStackTrace();
            }
          }
        });
  }

  private void loadTopPosts() {
    final Post.Query postsQuery = new Post.Query();
    postsQuery.getTop().withUser();

    postsQuery.findInBackground(
        new FindCallback<Post>() {
          @Override
          public void done(List<Post> posts, ParseException e) {
            if (e == null) {
              for (int i = 0; i < posts.size(); i++) {
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
                            .getUsername()); // user has been attached to the post
              }
            } else {
              e.printStackTrace();
            }
          }
        });
  }
}
