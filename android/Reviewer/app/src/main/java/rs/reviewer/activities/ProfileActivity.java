package rs.reviewer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.File;

import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.GenericFileProvider;
import rs.reviewer.utils.UserUtil;


public class ProfileActivity extends AppCompatActivity {

    private User user;

    private Button takePictureButton;
    private ImageView imageView;
    private Uri file;

    private String DIRECTORY_NAME = "PokemonGoStorage";
    private String FILE_NAME = "file";

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile);

            String userId = UserUtil.getLogInUser(getApplicationContext());
            user = new Gson().fromJson(userId, User.class);

            EditText editTextFirstName = findViewById(R.id.first_name);
            EditText editTextLastName = findViewById(R.id.last_name);

            editTextFirstName.setText(user.getName());
            editTextLastName.setText(user.getLastName());

            takePictureButton = (Button) findViewById(R.id.button_image);
            imageView = (ImageView) findViewById(R.id.logo);

            this.FILE_NAME = user.getEmail();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(false);
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            }

            File tmp = makeFileToUse();

            if(tmp.exists()){
                file = FileProvider.getUriForFile(this, GenericFileProvider.MY_PROVIDER, getOutputMediaFile());
                imageView.setImageURI(file);
            }
            else{
                //prikazi placeholder
            }

        }

        public void save(View view){

            EditText editTextFirstName = findViewById(R.id.first_name);
            EditText editTextLastName = findViewById(R.id.last_name);

            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();

            user.setName(firstName);
            user.setLastName(lastName);

            Call<ResponseBody> call = BaseService.userService.editUser(user.getId(), user);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {  //you can go on main page
                        Log.d("REZ", "Meesage SUCC");

                        try{
                            String userJson = response.body().string();
                            UserUtil.setLogInUser(userJson, getApplicationContext());
                        }
                        catch (Exception e){
                            Log.d("REZ", "Exception occured when parsing json");
                        }

                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    }
                    else{
                        Log.d("REZ","error: "+response.code());
                        Toast.makeText(ProfileActivity.this, R.string.bad_data, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    Toast.makeText(ProfileActivity.this, R.string.bad_data, Toast.LENGTH_LONG).show();
                }
            });
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), this.DIRECTORY_NAME);

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator +
                this.FILE_NAME + ".jpg");
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        file = FileProvider.getUriForFile(this, GenericFileProvider.MY_PROVIDER, getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(null); //invalidate cached image
                imageView.setImageURI(file);
            }
        }
    }

    private File makeFileToUse(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), this.DIRECTORY_NAME);

        return new File(mediaStorageDir.getPath() + File.separator +
                this.FILE_NAME + ".jpg");
    }

}
