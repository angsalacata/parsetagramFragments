package com.example.angsala.parsetagram;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.angsala.parsetagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class HomeTimelineActivity extends AppCompatActivity {

  String TAG = "HomeActivity";
  private EditText inputDescription;
  // private Button buttonRefresh;
  private Button buttonCreate;
  private Button buttonFeed;
  private ImageView testImage;
  static final int REQUEST_IMAGE_CAPTURE = 1;
  // this is from the codepath, capture intent article
  public final String APP_TAG = "MyParsetagram";
  public String photoFilename = "photo.jpg";
  File photofile;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.logout, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.logout:
        ParseUser user = ParseUser.getCurrentUser();
        user.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        System.out.println("CurrUser is " + currentUser);
        Log.d(TAG, "I clicked the icon");
        Intent i = new Intent(HomeTimelineActivity.this, ParsetagramActivity.class);
        startActivity(i);
        finish();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_timeline);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ParseObject.registerSubclass(Post.class);

    // identifying fragment by id

    inputDescription = (EditText) findViewById(R.id.inputDescription);
    // buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
    buttonCreate = (Button) findViewById(R.id.fragmentButtonCreate);
    buttonFeed = (Button) findViewById(R.id.buttonFeed);

    getSupportActionBar().setTitle("(F)instagram");

    // test for persistence
    ParseUser test_current_user = ParseUser.getCurrentUser();
    if (test_current_user != null) {
      Log.d(TAG, "Current user is good");
    } else {
      Intent logIn_intent = new Intent(HomeTimelineActivity.this, ParsetagramActivity.class);
      startActivity(logIn_intent);
      finish();
    }

    buttonCreate.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // instance objects for createPosts
            final String description = inputDescription.getText().toString();
            final ParseUser currentUser = ParseUser.getCurrentUser();

            final File file = getPhotoFileUri(photoFilename);
            final ParseFile picture = new ParseFile(file);
            // this ParseFile has to be saved
            picture.saveInBackground(
                new SaveCallback() {
                  @Override
                  public void done(ParseException e) {
                    createPosts(description, picture, currentUser);
                  }
                });
          }
        });

    buttonFeed.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent feedIntent = new Intent(HomeTimelineActivity.this, FeedActivity.class);
            // startActivity(feedIntent);
          }
        });

    // button to take pictures, it is a floating action button
    FloatingActionButton camera = (FloatingActionButton) findViewById(R.id.fab);
    camera.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            dispatchTakePictureIntent();
          }
        });

    bottomNavigation();
  }

  // Work with fragments, this should hopefully go to the feed
  public void bottomNavigation() {
    BottomNavigationView bottomNavigationView =
        (BottomNavigationView) findViewById(R.id.bottom_navigation);
    //    final FragmentManager fragmentManager = getSupportFragmentManager();
    //    final Fragment fragmentFeed = new FeedFragment();

    // put a listener on bottom
    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_feeds:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.your_placeholder, new FeedFragment());
                ft.commit();
                return true;
              case R.id.action_home:
                FragmentTransaction fthome = getSupportFragmentManager().beginTransaction();
                fthome.replace(R.id.your_placeholder, new HomeFragment());
                fthome.commit();
                return true;
            }

            return false;
          }
        });
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
              inputDescription.setText(null);
              testImage.setImageResource(R.color.colorPrimaryDark);
              // TODO-this still doesn't show
              // Toast.makeText(getApplicationContext(), "Posted!", Toast.LENGTH_LONG);
            } else {
              e.printStackTrace();
            }
          }
        });
  }

  private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    photofile = getPhotoFileUri(photoFilename);
    Uri fileProvider =
        FileProvider.getUriForFile(
            HomeTimelineActivity.this, "com.codepath.fileprovider", photofile);
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      super.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  private File getPhotoFileUri(String photoFilename) {
    File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
    // create storage directory if not in existence
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
      Log.d(APP_TAG, "failed to create directory");
    }
    File file = new File(mediaStorageDir.getPath() + File.separator + photoFilename);
    return file;
  }

  // use this to trigger that the file was loaded
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    //    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
    //      Log.d(TAG, "successfully saved photo");
    //      // this will still load the pic into an imageview
    //      Bitmap image = BitmapFactory.decodeFile(photofile.getAbsolutePath());
    //
    //      testImage = (ImageView) findViewById(R.id.imvTestGettingCamera);
    //      testImage.setImageBitmap(image);
    //
    //    } else {
    //      Toast.makeText(this, "Picture wasn't taken ACTIVITY!", Toast.LENGTH_SHORT).show();
    //    }
  }
}
