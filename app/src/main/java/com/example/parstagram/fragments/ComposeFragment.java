package com.example.parstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.MainActivity;
import com.example.parstagram.functionalityClasses.BitmapScaler;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {
    private final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    protected static final String AUTHORITY_PROVIDER =  "com.codepath.fileprovider";
    EditText etPhotoDescription;
    Button btTakePhoto;
    ImageView ivPhotoTaken;
    Button btSubmitPhoto;
    File photoFile;
    String photoFileName = "photo.jpg";
    MenuItem miActionProgressItem;

    public ComposeFragment(){}

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_compose, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        etPhotoDescription = view.findViewById(R.id.etPhotoDescription);
        btTakePhoto = view.findViewById(R.id.btTakePhoto);
        ivPhotoTaken = view.findViewById(R.id.ivImageTaken);
        btSubmitPhoto  = view.findViewById(R.id.btSubmitPhoto);

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btSubmitPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etPhotoDescription.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(getContext(), "sorry description can't be empty", Toast.LENGTH_SHORT);
                    return;
                }
                if(photoFile == null || ivPhotoTaken.getDrawable() == null){
                    Toast.makeText(getContext(), "there is no image :(", Toast.LENGTH_SHORT);
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                Log.e(TAG, "running savePost method");
                savePost(description, currentUser, photoFile);
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), ComposeFragment.AUTHORITY_PROVIDER, photoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, resizedFileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                // See code above

                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 255);
                //ivPhotoTaken.setImageBitmap(takenImage);
                ivPhotoTaken.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        MainActivity.showProgressBar();
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "post saving failed!");
                    Toast.makeText(getContext(), "post failed try again", Toast.LENGTH_SHORT);
                    MainActivity.hideProgressBar();
                    return;
                }
                Log.i(TAG, "post succeded");
                etPhotoDescription.setText("");
                ivPhotoTaken.setImageResource(0);
                MainActivity.hideProgressBar();
            }
        });
    }
}