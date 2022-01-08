package edu.rice.comp504.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Response {
    private int errCode;
    private String errMsg;
    private JsonArray data;

    /**
     * Response constructor.
     */
    public Response(int errCode, String errMsg, JsonArray data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public JsonArray getData() {
        return data;
    }
}
