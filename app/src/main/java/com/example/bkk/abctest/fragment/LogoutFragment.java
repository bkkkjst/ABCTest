package com.example.bkk.abctest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bkk.abctest.R;
import com.example.bkk.abctest.manager.HttpManager;
import com.example.bkk.abctest.model.LogoutResponse;

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
public class LogoutFragment extends Fragment {

    @BindView(R.id.tvUserId)
    TextView mTvUserId;
    @BindView(R.id.tvToken)
    TextView mTvToken;
    @BindView(R.id.btnLogout)
    Button mBtnLogout;
    Unbinder unbinder;

    private static final String TAG = "LogoutFragment";

    public LogoutFragment() {
        super();
    }

    private String accessToken = "";

    public static LogoutFragment newInstance() {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (getArguments() != null) {
            accessToken = getArguments().getString("token", "");
        }

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        initInstances(rootView, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getActivity().getSharedPreferences("user-data", Context.MODE_PRIVATE);
        String userId = pref.getString("userId", "");

        mTvToken.setText("Token: "+accessToken);
        mTvUserId.setText("UserId: "+userId);
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

    @OnClick(R.id.btnLogout)
    public void onViewClicked() {

//        logout();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, LoginFormFragment.newInstance())
                .commit();

    }

    private void logout() {
        HttpManager.getInstance().getService().logout()
                .enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "onResponse: logout");
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + t.toString());
                    }
                });
    }
}
