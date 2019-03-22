package com.scubasnsi.mysnsi.model.data_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scubasnsi.mysnsi.model.dto.ActionDto;

import static com.scubasnsi.mysnsi.app.AppGlobalConstant.DEFAULT_TEMP_VALUE;

/**
 * Author -
 * Date -  17-04-2017.
 */
/*
* "action":"add_dive",
"user_id":"4131",
"max_depth" : "500",
"dive_for" : "fun",
"dive_type" : "technical",

"date"	: "2017-01-18",
"dive_time_in" : "10:30",
"dive_time_out" : "11:15",
"dive_time_total" : "45",

"location":"weeee",
"latitude" : "0.23445454545",
"longitude" : "0.23445454545",


"dive_site" : "site name",
"buddy_name" : "Buddy name"
"instructor_flag" : "Instructor",
"instructor_info" : "Instructor name",
"dive_center_name" : "Scuba diving",
"note" : "Note here",

"tank_pressure_in": "45",
"tank_pressure_out" : "30",
"tank_pressure_used" : "15",
"equipment_weight" : "26",
"equipment_suit" : "lorem ipsom",
"equipment_tank" : "test",  //What is this 



"wheather" : "Sunny",
"visibility" : "clear",
"air_temperature" : "25",
"water_temperature" : "15",


"gas_optional" : "yes", //Pending
"decompression_optional" : "no",  //Pending


}


      "equipment_tank": "", //What is this
      "gasType": "air-21-78", //What is this
      "decompression": "",  //Pending
      "date_added": "2017-04-18 02:33:32" //Pending


*
* */
public class PassportUpload extends ActionDto implements Cloneable {

    @Expose
    @SerializedName("user_id")
    private long mUserId;

    public PassportUpload(String action) {
        super(action);
    }


    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }



    @Override
    public String toString() {
        return "Logbook{" +
                "mUserId=" + mUserId +
               '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
