package com.example.angsala.parsetagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angsala.parsetagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
  ImageView detailsImage;
  TextView detailsDescription;
  TextView detailsCreatedAt;
  TextView detailsUsername;
  String textDescription;
  String textUsername;
  String textCreatedAt;
  File imageFile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);

    detailsImage = (ImageView) findViewById(R.id.imvDetailsImage);
    detailsDescription = (TextView) findViewById(R.id.txtvDetailsDescription);
    detailsCreatedAt = (TextView) findViewById(R.id.txtvCreatedAt);
    detailsUsername = (TextView) findViewById(R.id.txtvUsername);

    final Post receivedPost =
        (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

    try {
      imageFile = receivedPost.getImage().getFile();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    detailsImage.setImageBitmap(image);
    textDescription = receivedPost.getDescription();
    textCreatedAt = receivedPost.getCreatedAt().toString();
    ParseUser postuser = receivedPost.getUser();
    textUsername = postuser.getUsername();
    Date createdAt = receivedPost.getCreatedAt();
    String date =
        DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
            .toString();
    detailsDescription.setText(textUsername + " " + textDescription);
    detailsCreatedAt.setText(date);
    detailsUsername.setText(textUsername);
  }
}
