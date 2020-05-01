package rs.reviewer.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import rs.reviewer.utils.UserUtil;


public class ProfileActivity extends AppCompatActivity {

    private User user;

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

    }
