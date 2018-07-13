package com.example.angsala.parsetagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    // this will create a config
    // question, is there any difference in the parse.config metthod vs the Okhttp client
    final Parse.Configuration configuration =
        new Parse.Configuration.Builder(this)
            .applicationId("angsala")
            .clientKey(this.getString(R.string.APP_ID)) // password
            .server(this.getString(R.string.server)) // server
            .build();

    Parse.initialize(configuration);
  }
}
