package com.example.bkk.abctest.fragment;

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
import com.example.bkk.abctest.model.LoginResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class LoginFormFragment extends Fragment {

    private static final String TAG = "LoginFormFragment";

    @BindView(R.id.text_input_edit_text_email)
    TextInputEditText mTextInputEditTextEmail;
    @BindView(R.id.text_input_email)
    TextInputLayout mTextInputEmail;
    @BindView(R.id.text_input_edit_text_password)
    TextInputEditText mTextInputEditTextPassword;
    @BindView(R.id.text_input_password)
    TextInputLayout mTextInputPassword;
    @BindView(R.id.btnLogin)
    Button mBtnButton;
    Unbinder unbinder;

    private String token = "";

    boolean checkEmail = false;
    boolean checkPassword = false;

    public LoginFormFragment() {
        super();
    }

    public static LoginFormFragment newInstance() {
        LoginFormFragment fragment = new LoginFormFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_login_form, container, false);
        initInstances(rootView, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void validation() {

        final String PASSWORD_PATTERN = "(?=.*?[A-Z])(?=.*?[a-z]).{6,}";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        String email = mTextInputEditTextEmail.getText().toString();
        String password = mTextInputEditTextPassword.getText().toString();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "password");
        requestBody.put("username", email);
        requestBody.put("password", password);

        checkEmail = isValidationEmail(email);
        checkPassword = isValidationPassword(pattern, password);


        if (checkEmail && checkPassword) {

            login(requestBody);
        }
    }

    private void login(Map<String, String> requestBody) {
        HttpManager.getInstance().getService()
                .login(requestBody)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {

                            token = response.body().getAccess_token();

                            Bundle bundle = new Bundle();
                            bundle.putString("token", token);
                            LogoutFragment fragment = new LogoutFragment();
                            fragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentContainer, fragment, "LoginFragment")
                                    .commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + t.toString());
                    }
                });
    }

    private boolean isValidationEmail(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mTextInputEmail.setErrorEnabled(true);
            mTextInputEmail.setError(
                    "Not empty, please fill data.\n" +
                            "Email pattern do not match");
            mTextInputEmail.requestFocus();
            return false;
        } else {
            mTextInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidationPassword(Pattern pattern, String password) {
        if (TextUtils.isEmpty(password) || password.length() < 5 || !pattern.matcher(password).matches()) {
            mTextInputPassword.setErrorEnabled(true);
            mTextInputPassword.setError(
                    "Not empty, please fill data.\n" +
                            "your password can use A-Z, a-z, 0-9\n" +
                            "your password at least 6 character\n" +
                            "your password at least one uppercase character\n" +
                            "your password at least one lowercase character\n");


            mTextInputPassword.requestFocus();
            return false;
        } else {
            mTextInputPassword.setErrorEnabled(false);
            return true;
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked() {
        validation();
    }

}
