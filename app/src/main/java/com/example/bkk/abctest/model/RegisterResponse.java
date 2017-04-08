package com.example.bkk.abctest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by BKK on 7/4/2560.
 */

public class RegisterResponse {

    private String userId;
    private String message;
    private ModelStateBean modelState;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelStateBean getModelState() {
        return modelState;
    }

    public void setModelState(ModelStateBean modelState) {
        this.modelState = modelState;
    }

    public static class ModelStateBean {
        @SerializedName("createUserModel.Email")
        private List<String> email; // FIXME check this code
        @SerializedName("createUserModel.Password")
        private List<String> password; // FIXME check this code
        @SerializedName("createUserModel.ConfirmPassword")
        private List<String> confirmPassword; // FIXME check this code

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }

        public List<String> getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(List<String> confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
