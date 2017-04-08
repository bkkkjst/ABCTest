package com.example.bkk.abctest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bkk.abctest.R;
import com.example.bkk.abctest.manager.HttpManager;
import com.example.bkk.abctest.model.Register;
import com.example.bkk.abctest.model.RegisterResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class RegistrationFragment extends Fragment {

    private static final String TAG = "RegistrationFragment";

    boolean checkEmail = false;
    boolean checkRoleName = false;
    boolean checkPassword = false;
    boolean checkConfirmPassword = false;

    Button btnRegister;
    TextInputEditText txtExtEmail, txtExtRoleName, txtExtPassword, txtExtConfirmPassword;
    TextInputLayout txtLayoutEmail, txtLayoutRoleName, txtLayoutPassword, txtLayoutConfirmPassword;

    public RegistrationFragment() {
        super();
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState


        btnRegister = (Button) rootView.findViewById(R.id.btn_register);

        // TextInputEditText
        txtExtEmail = (TextInputEditText) rootView.findViewById(R.id.text_input_edit_text_email);
        txtExtRoleName = (TextInputEditText) rootView.findViewById(R.id.text_input_edit_text_role_name);
        txtExtPassword = (TextInputEditText) rootView.findViewById(R.id.text_input_edit_text_password);
        txtExtConfirmPassword = (TextInputEditText) rootView.findViewById(R.id.text_input_edit_text_confirm_password);

        // TextInputLayout
        txtLayoutEmail = (TextInputLayout) rootView.findViewById(R.id.text_input_email);
        txtLayoutRoleName = (TextInputLayout) rootView.findViewById(R.id.text_input_role_name);
        txtLayoutPassword = (TextInputLayout) rootView.findViewById(R.id.text_input_password);
        txtLayoutConfirmPassword = (TextInputLayout) rootView.findViewById(R.id.text_input_email_confirm_password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private void validation() {

        final String PASSWORD_PATTERN = "(?=.*?[A-Z])(?=.*?[a-z]).{6,}";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        String email = txtExtEmail.getText().toString();
        String roleName = txtExtRoleName.getText().toString();
        String password = txtExtPassword.getText().toString();
        String confirmPassword = txtExtConfirmPassword.getText().toString();

        Register register = new Register();
        register.setEmail(email);
        register.setRoleName(roleName);
        register.setPassword(password);
        register.setConfirmPassword(confirmPassword);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", register.getEmail());
        requestBody.put("roleName", register.getRoleName());
        requestBody.put("password", register.getPassword());
        requestBody.put("confirmPassword", register.getConfirmPassword());

        checkEmail = isValidationEmail(email);
        checkRoleName = isValidationRoleName(roleName);
        checkPassword = isValidationPassword(pattern, password);
        checkConfirmPassword = isValidationConfirmPassword(password, confirmPassword);

        if(checkEmail && checkRoleName && checkPassword && checkConfirmPassword){

            registration(requestBody);
        }


    }

    private void registration(Map<String, String> register) {

        HttpManager.getInstance().getService()
                .registration(register)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                        if(response.isSuccessful()){

                            if(response.body().getMessage() == null){

                                SharedPreferences pref = getActivity().getSharedPreferences("user-data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("userId", response.body().getUserId());
                                editor.apply();

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentContainer, RegistrationResultFragment.newInstance(), "RegistrationResultFragment")
                                        .commit();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Log.i(TAG, "onFailure: "+t.toString());
                    }
                });
    }

    private boolean isValidationConfirmPassword(String password, String confirmPassword) {
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            txtLayoutConfirmPassword.setErrorEnabled(true);
            txtLayoutConfirmPassword.setError(
                    "Not empty, please fill data.\n" +
                            "your password do not match");
            txtLayoutConfirmPassword.requestFocus();
            return false;
        } else {
            txtLayoutConfirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidationPassword(Pattern pattern, String password) {
        if (TextUtils.isEmpty(password) || password.length() < 5 || !pattern.matcher(password).matches()) {
            txtLayoutPassword.setErrorEnabled(true);
            txtLayoutPassword.setError(
                    "Not empty, please fill data.\n" +
                            "your password can use A-Z, a-z, 0-9\n" +
                            "your password at least 6 character\n" +
                            "your password at least one uppercase character\n" +
                            "your password at least one lowercase character\n");


            txtLayoutPassword.requestFocus();
            return false;
        } else {
            txtLayoutPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidationRoleName(String roleName) {
        if (TextUtils.isEmpty(roleName)) {
            txtLayoutRoleName.setErrorEnabled(true);
            txtLayoutRoleName.setError("Not empty, please fill data.");
            txtLayoutRoleName.requestFocus();
            return false;
        } else {
            txtLayoutRoleName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidationEmail(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtLayoutEmail.setErrorEnabled(true);
            txtLayoutEmail.setError(
                    "Not empty, please fill data.\n" +
                            "Email pattern do not match");
            txtLayoutEmail.requestFocus();
            return false;
        } else {
            txtLayoutEmail.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
