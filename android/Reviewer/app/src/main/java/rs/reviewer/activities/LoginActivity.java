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

import model.LoginDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.reviewer.MainActivity;
import rs.reviewer.R;
import rs.reviewer.rest.BaseService;
import rs.reviewer.utils.UserUtil;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void tapToPlay(View view){

        EditText editTextEmail = findViewById(R.id.email);
        EditText editTextPassword = findViewById(R.id.password);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);


        Call<ResponseBody> call = BaseService.userService.login(loginDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){  //you can go on main page
                    Log.d("REZ","Meesage SUCC");

                    try{
                        String userJson = response.body().string();
                        UserUtil.setLogInUser(userJson, getApplicationContext());

                    }
                    catch (Exception e){
                        Log.d("REZ","Meesage Exception occured when parsing user");
                    }


                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // because user can not go back to log in while he is logged in on app
                }else{
                    Log.d("REZ","error: "+response.code());
                    Toast.makeText(LoginActivity.this, R.string.wrong_credentials, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                Toast.makeText(LoginActivity.this, R.string.wrong_credentials, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void haveNoAccount(View view){

        TextView txtView = findViewById(R.id.haveNoAccount);
        String text = txtView.getText().toString();
        txtView.setText(Html.fromHtml("<u>" + text + "</u>"));

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }
}
