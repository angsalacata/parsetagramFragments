package com.example.angsala.parsetagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.angsala.parsetagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface to handle interaction events. Use
 * the {@link HomeFragment#newInstance} factory method to create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  static String TAG = "HomeFragment";
  private EditText inputDescription;
  // private Button buttonRefresh;
  private Button buttonCreate;
  private ImageView testImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    // this is from the codepath, capture intent article
    public final String APP_TAG = "MyParsetagram";
    public String photoFilename = "photo.jpg";
    File photofile;

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public HomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static HomeFragment newInstance(String param1, String param2) {
    HomeFragment fragment = new HomeFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //inflating views in fragment
    inputDescription = (EditText) getActivity().findViewById(R.id.fragmentInputDescription);
    buttonCreate = (Button) getActivity().findViewById(R.id.fragmentButtonCreate);
    testImage = (ImageView) getActivity().findViewById(R.id.fragmentTestGettingCamera);



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



      // button to take pictures, it is a floating action button
      FloatingActionButton camera = (FloatingActionButton) getActivity().findViewById(R.id.fab);
      camera.setOnClickListener(
              new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      dispatchTakePictureIntent();
                  }
              });

  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

//  @Override
//  public void onAttach(Context context) {
//    super.onAttach(context);
//    if (context instanceof OnFragmentInteractionListener) {
//      mListener = (OnFragmentInteractionListener) context;
//    } else {
//      throw new RuntimeException(
//          context.toString() + " must implement OnFragmentInteractionListener");
//    }
//  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  //  public v

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity.
   *
   * <p>See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }



  //start Camera

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photofile = getPhotoFileUri(photoFilename);
        Uri fileProvider =
                FileProvider.getUriForFile(
                        getActivity(), "com.codepath.fileprovider", photofile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private File getPhotoFileUri(String photoFilename) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        // create storage directory if not in existence
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFilename);
        return file;
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

    // use this to trigger that the file was loaded
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG, "successfully saved photo");
            // this will still load the pic into an imageview
            Bitmap image = BitmapFactory.decodeFile(photofile.getAbsolutePath());

            testImage = (ImageView) getActivity().findViewById(R.id.fragmentTestGettingCamera);
            Toast.makeText(getActivity(), "Picture was taken", Toast.LENGTH_SHORT).show();
            testImage.setImageBitmap(image);

        } else {
            Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }
}
