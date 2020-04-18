package rs.reviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;


public class ProfileActivity extends Activity {

    private User user;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile);

            Call<ResponseBody> call = BaseService.userService.findById(8L);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Log.d("REZ", "Meesage SUCC");
                        try{
                            user = new Gson().fromJson(response.body().string(), User.class);

                            EditText editTextFirstName = findViewById(R.id.first_name);
                            EditText editTextLastName = findViewById(R.id.last_name);

                            editTextFirstName.setText(user.getName());
                            editTextLastName.setText(user.getLastName());
                        }
                        catch (Exception e){
                            Log.d("REZ","error Pri konvertovanju json-a");
                        }

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

                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        finish(); // because user can not go back to log in while he is logged in on app
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


    }
