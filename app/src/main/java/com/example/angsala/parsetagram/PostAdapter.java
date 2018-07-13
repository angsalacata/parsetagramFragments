package com.example.angsala.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angsala.parsetagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
  public static final String POST_ID = "objectId";
  private List<Post> adapterPosts;
  Context context;
  File imageFile;

  public PostAdapter(List<Post> postsIn) {
    this.adapterPosts = postsIn;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View postView = inflater.inflate(R.layout.item_post, parent, false);
    return new ViewHolder(postView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final Post post = adapterPosts.get(position);
    ParseUser user = post.getUser();
    String username = user.getUsername();
    Date createdAt = post.getCreatedAt();
    String timestamp =
        DateUtils.getRelativeTimeSpanString(
                createdAt.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
            .toString();
    try {
      imageFile = post.getImage().getFile();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    holder.imvImage.setImageBitmap(image);
    holder.txtvUsername.setText("Created by " + username);
    holder.txtvTimestamp.setText(timestamp);
  }

  @Override
  public int getItemCount() {
    return adapterPosts.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView imvImage;
    TextView txtvUsername;
    TextView txtvTimestamp;

    public ViewHolder(View itemView) {
      super(itemView);
      imvImage = (ImageView) itemView.findViewById(R.id.imvPicture);
      txtvUsername = (TextView) itemView.findViewById(R.id.txtvUsernameScroll);
      txtvTimestamp = (TextView) itemView.findViewById(R.id.txtvTimestampScroll);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      int viewPosition = getAdapterPosition();
      if (viewPosition != RecyclerView.NO_POSITION) {
        Post post = adapterPosts.get(viewPosition);

        Intent detailsIntent = new Intent(context, DetailsActivity.class);
        detailsIntent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
        context.startActivity(detailsIntent);
      }
    }
  }

  // clean all elements
  public void clear() {
    adapterPosts.clear();
    notifyDataSetChanged();
  }

  public void addAll(List<Post> list) {
    adapterPosts.addAll(list);
    notifyDataSetChanged();
  }
}
