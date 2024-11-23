package com.group1.scansaver.api;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UPCApiRequest {

    public interface UPCApiResponseCallback {
        void onSuccess(String barcode, String title, String msrp, String stores);
        void onError(String error);
    }

    public void fetchProductDetails(String barcode, UPCApiResponseCallback callback) {

        String API_URL = "https://api.upcdatabase.org/product/";

        API_URL = API_URL + barcode;

        Log.e("API","API URL: "+API_URL);

        OkHttpClient client = new OkHttpClient();

        // NOT REAL KEY, KEY MUST GO IN SAFE SPACE
        String API_KEY = ""; // API KEY

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.e("API", "Response Body: " + responseBody);
                    parseResponse(responseBody, callback);
                } else {
                    String errorBody = response.body().string();
                    callback.onError("Request failed with code: " + response.code() + ", body: " + errorBody);
                    Log.e("API", "FAILURE-API: " + response.code() + ", Body: " + errorBody);
                }
                //LOGGING BELOW
            } catch (IOException e) {
                Log.e("API", "IOException occurred: " + e.getMessage());
                callback.onError("IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.e("API", "JSONException occurred: " + e.getMessage());
                callback.onError("JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.e("API", "Unexpected Exception: " + e.getMessage());
                callback.onError("Unexpected error: " + e.getMessage());
            }
        }).start();
    }

    private void parseResponse(String responseBody, UPCApiResponseCallback callback) throws JSONException {
        JSONObject jsonResponse = new JSONObject(responseBody);

        String barcode = jsonResponse.optString("barcode", "N/A");
        String title = jsonResponse.optString("title", "N/A");
        String msrp = jsonResponse.optString("msrp", "N/A");

        JSONArray storesArray = jsonResponse.optJSONArray("stores");
        StringBuilder storesBuilder = new StringBuilder();
        if (storesArray != null) {
            for (int i = 0; i < storesArray.length(); i++) {
                JSONObject storeObject = storesArray.getJSONObject(i);
                String storeName = storeObject.optString("store", "Unknown Store");
                String storePrice = storeObject.optString("price", "N/A");
                storesBuilder.append(storeName).append(" - $").append(storePrice).append("\n");
            }
        }
        callback.onSuccess(barcode, title, msrp, storesBuilder.toString());
    }
}

