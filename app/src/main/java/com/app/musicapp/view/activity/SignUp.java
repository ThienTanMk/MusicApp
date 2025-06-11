package com.app.musicapp.view.activity;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.musicapp.R;
import com.app.musicapp.api.ApiClient;
import com.app.musicapp.model.request.UserCreationRequest;
import com.app.musicapp.model.response.ApiResponse;
import com.app.musicapp.model.response.CheckUsernameResponse;
import com.app.musicapp.model.response.UserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    LinearLayout linear_username, linear_password, linear_email, linear_firstname, linear_lastname, linear_displayname, linear_gender, linear_birthday;
    EditText edit_username, edit_password, edit_text_email, edit_text_firstname, edit_text_lastname, edit_text_displayname, edit_text_birthday;
    Button button_sigup;
    Spinner spinner_gender;

    UserCreationRequest userCreationRequest = new UserCreationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();

    }

    private void passwordView(){
        edit_username.setEnabled(false);
        linear_password.setVisibility(View.VISIBLE);
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
        button_sigup.setOnClickListener(v->{
            String password = edit_password.getText().toString();
            if(password.length()<8){
                edit_password.setError("Password must be at least 8 characters");
                return;
            }
            userCreationRequest.setPassword(password);
            infoView();
        });
    }
    private LocalDate stringToLocalDate(String dateString){
        try {
            // Define the formatter for dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parse the string to LocalDate
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            return  localDate;
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }
    private void infoView(){

        linear_username.setVisibility(View.GONE);
        linear_password.setVisibility(View.GONE);
        linear_email.setVisibility(View.VISIBLE);
        linear_firstname.setVisibility(View.VISIBLE);
        linear_lastname.setVisibility(View.VISIBLE);
        linear_displayname.setVisibility(View.VISIBLE);
        linear_gender.setVisibility(View.VISIBLE);
        linear_birthday.setVisibility(View.VISIBLE);

        button_sigup.setText("Sig Up");

        button_sigup.setOnClickListener(v->{
            String email = edit_text_email.getText().toString();
            String firstname = edit_text_firstname.getText().toString();
            String lastname = edit_text_lastname.getText().toString();
            String displayname = edit_text_displayname.getText().toString();
            String gender = spinner_gender.getSelectedItem().toString();
            String birthday = edit_text_birthday.getText().toString();
            if(email.isEmpty()){
                edit_text_email.setError("Email is required");
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.setError("Email is invalid");
                return;
            }
            if(firstname.isEmpty()){
                edit_text_firstname.setError("Firstname is required");
                return;
            }
            if(lastname.isEmpty()){
                edit_text_lastname.setError("Lastname is required");
            }
            if(displayname.isEmpty()){
                edit_text_displayname.setError("Displayname is required");
                return;
            }
            if(birthday.isEmpty()){
                edit_text_birthday.setError("Birthday is required");
                if(stringToLocalDate(birthday)==null)
                    edit_text_birthday.setError("Birthday is invalid");
                return;
            }
            if(spinner_gender.getSelectedItem()==null){
                Toast.makeText(SignUp.this, "Gender is required", Toast.LENGTH_LONG).show();
                return;
            }
            userCreationRequest.setEmail(email);
            userCreationRequest.setFirstName(firstname);
            userCreationRequest.setLastName(lastname);
            userCreationRequest.setDisplayName(displayname);
            userCreationRequest.setGender(gender);
            userCreationRequest.setDob(stringToLocalDate(birthday));

            ApiClient.getIdentityService().signUp(userCreationRequest).enqueue(new Callback<ApiResponse<UserResponse>>() {

                @Override
                public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(SignUp.this, "Sign Up successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        int code = jsonObject.getInt("code");
                        switch (code){
                            case 1014:
                                edit_text_email.setError(message);
                                break;
                            case 1017: edit_text_displayname.setError(message);
                                break;
                            default:
                                Toast.makeText(SignUp.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (Exception ex){
                        Toast.makeText(SignUp.this, "Can not sign up now.Try later", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {

                }
            });
        });

    }
    private void initView(){
        linear_username = findViewById(R.id.linear_username);
        linear_password = findViewById(R.id.linear_password);
        linear_email = findViewById(R.id.linear_email);
        linear_firstname = findViewById(R.id.linear_firstname);
        linear_lastname = findViewById(R.id.linear_lastname);
        linear_displayname = findViewById(R.id.linear_displayname);
        linear_gender = findViewById(R.id.linear_gender);
        linear_birthday = findViewById(R.id.linear_birthday);
        linear_username.setVisibility(View.VISIBLE);
        button_sigup = findViewById(R.id.button_sigup);


        linear_password.setVisibility(View.GONE);
        linear_email.setVisibility(View.GONE);
        linear_firstname.setVisibility(View.GONE);
        linear_lastname.setVisibility(View.GONE);
        linear_displayname.setVisibility(View.GONE);
        linear_gender.setVisibility(View.GONE);
        linear_birthday.setVisibility(View.GONE);

        edit_username = findViewById(R.id.edit_username);
        edit_password = findViewById(R.id.edit_password);
        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_firstname = findViewById(R.id.edit_text_firstname);
        edit_text_lastname = findViewById(R.id.edit_text_lastname);
        edit_text_displayname = findViewById(R.id.edit_text_displayname);
        edit_text_birthday = findViewById(R.id.edit_text_birthday);
        spinner_gender = findViewById(R.id.spinner_gender);

        edit_username.addTextChangedListener(new TextWatcher() {
            private boolean isUpdate = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String original = edit_username.getText().toString();
                String clean = original.replaceAll("[^a-zA-Z0-9]", "");
                if(!original.equals(clean)){
                    edit_username.setText(clean);
                    edit_username.setSelection(clean.length());
                }

            }
        });
        edit_text_birthday.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edit_text_birthday.setText(current);
                    edit_text_birthday.setSelection(sel < current.length() ? sel : current.length());
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });


        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter);

        button_sigup.setOnClickListener(v->{
            String username = edit_username.getText().toString();
            if(username.length()<3){
                edit_username.setError("Username must be at least 3 characters");
            }
            if(username.contains(" ")){
                edit_username.setError("Spaces are not allow in username");
                return;
            }
            ApiClient.getIdentityService().checkUsername(username).enqueue(new Callback<ApiResponse<CheckUsernameResponse>>() {

                @Override
                public void onResponse(Call<ApiResponse<CheckUsernameResponse>> call, Response<ApiResponse<CheckUsernameResponse>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getData().isExisted()){
                            edit_username.setError("Username is existed");
                        }
                        else{
                            userCreationRequest.setUsername(username);
                            passwordView();
                        }
                    }
                    else{
                        Toast.makeText(SignUp.this, "Can not sign up now.Try later", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<CheckUsernameResponse>> call, Throwable t) {}
            });
        });
    }
}
