package com.adms.employeeclock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admsandroid on 3/12/2018.
 */

public class EmployeeModel {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("EmployeeName")
    @Expose
    private String employeeName;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
