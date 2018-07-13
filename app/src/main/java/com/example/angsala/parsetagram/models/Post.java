package com.example.angsala.parsetagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

// this is the name of the custom class that we made in parse
@ParseClassName("Post")
public class Post extends ParseObject {
  // same as the columns
  private static String DESCRIPTION_KEY = "Description";
  private static String IMAGE_KEY = "Image";
  private static String USER_KEY = "User";

  public void setDescription(String description) {
    put(DESCRIPTION_KEY, description);
  }

  public String getDescription() {
    return getString(DESCRIPTION_KEY);
  }

  public void setImage(ParseFile image) {
    put(IMAGE_KEY, image);
  }

  public ParseFile getImage() {
    return getParseFile(IMAGE_KEY);
  }

  public void setUser(ParseUser user) {
    put(USER_KEY, user);
  }

  public ParseUser getUser() {
    return getParseUser(USER_KEY);
  }

  public static class Query extends ParseQuery<Post> {

    public Query() {
      super(Post.class);
    }
    public Query getTop(){
      //called the builder pattern, allow top 20 images to appear
        setLimit(20);
        return this;
    }
    public Query withUser(){
      include("User");
      return this;
    }
  }
}
