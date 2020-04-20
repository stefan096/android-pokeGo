package rs.reviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

    }

    public void register(View view){

        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);
        EditText editTextFirstName = findViewById(R.id.first_name);
        EditText editTextLastName = findViewById(R.id.last_name);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(firstName);
        user.setLastName(lastName);


        Call<ResponseBody> call = BaseService.userService.register(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201){  //you can go on main page
                    Log.d("REZ","Meesage SUCC");

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else if (response.code() == 406 || response.code() == 423){
                    Toast.makeText(RegisterActivity.this, R.string.bad_data, Toast.LENGTH_LONG).show();

                } else if (response.code() == 400){ //not unique email
                    Toast.makeText(RegisterActivity.this, R.string.email_already_exist, Toast.LENGTH_LONG).show();
                }
                else{
                    Log.d("REZ","error: "+response.code());
                    Toast.makeText(RegisterActivity.this, R.string.bad_data, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                Toast.makeText(RegisterActivity.this, R.string.bad_data, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void alreadyHaveAccount(View view){

        TextView txtView = findViewById(R.id.already_have_account);
        String text = txtView.getText().toString();
        txtView.setText(Html.fromHtml("<u>" + text + "</u>"));

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

    }
}
