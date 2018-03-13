package com.adms.employeeclock.Utility;

import com.adms.employeeclock.EmployeeModel;

import java.util.Map;


import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
/**
 * Created by admsandroid on 3/12/2018.
 */

public interface Webservice {
    @FormUrlEncoded
    @POST("/Employee_Clock_In_Out_Log")
    public void getEmpolyeeAttendace(@FieldMap Map<String, String> map, Callback<EmployeeModel> callback);
}
