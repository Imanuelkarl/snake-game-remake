package com.regensnakevsblock.sbb.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestersTrackers {

    private static final String TAG = "TestersTrackers";
    private static final String PREFS_NAME = "tester_tracker_prefs";
    private static final String KEY_DEVICE_ID = "device_unique_id";
    private static final String KEY_LAST_SYNC_DATE = "last_sync_date";
    private static final String KEY_PENDING_LOGS = "pending_logs";
    private static final String KEY_DEVICE_MODEL = "device_model";
    private static final String KEY_ANDROID_VERSION = "android_version";
    private static final String KEY_APP_VERSION = "app_version";

    private static final String SERVER_URL = "https://your-server.com/api/tracker"; // Replace with your server URL
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Context context;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private String deviceId;

    public TestersTrackers(Context context) {
        this.context = context.getApplicationContext();
        this.client = new OkHttpClient();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.deviceId = getOrCreateDeviceId();
    }

    /**
     * Get or create a unique device identifier
     */


    /**
     * Save device information for tracking
     */
    private void saveDeviceInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DEVICE_MODEL, android.os.Build.MODEL);
        editor.putString(KEY_ANDROID_VERSION, android.os.Build.VERSION.RELEASE);

        try {
            String appVersion = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .versionName;
            editor.putString(KEY_APP_VERSION, appVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error getting app version", e);
        }

        editor.apply();
    }


    /**
     * Track app launch - this should be called when the main activity loads
     */
    public void trackAppLaunch() {
        String today = getTodayDate();
        String lastSync = sharedPreferences.getString(KEY_LAST_SYNC_DATE, "");

        // Check if we already tracked today
        if (today.equals(lastSync)) {
            Log.d(TAG, "Already tracked today for device: " + deviceId);
            return;
        }

        // Create log entry
        JSONObject logEntry = createLogEntry(today);

        // Store locally
        storeLogLocally(logEntry);

        // Update last sync date
        sharedPreferences.edit().putString(KEY_LAST_SYNC_DATE, today).apply();

        // Try to sync with server if online
        syncWithServer();
    }

    /**
     * Create a log entry
     */
    private JSONObject createLogEntry(String date) {
        JSONObject entry = new JSONObject();
        try {
            entry.put("deviceId", deviceId);
            entry.put("date", date);
            entry.put("timestamp", System.currentTimeMillis());
            entry.put("deviceModel", sharedPreferences.getString(KEY_DEVICE_MODEL, "Unknown"));
            entry.put("androidVersion", sharedPreferences.getString(KEY_ANDROID_VERSION, "Unknown"));
            entry.put("appVersion", sharedPreferences.getString(KEY_APP_VERSION, "Unknown"));
            entry.put("action", "app_launch");
        } catch (JSONException e) {
            Log.e(TAG, "Error creating log entry", e);
        }
        return entry;
    }

    /**
     * Store log entry locally
     */
    private void storeLogLocally(JSONObject logEntry) {
        try {
            String existingLogs = sharedPreferences.getString(KEY_PENDING_LOGS, "[]");
            JSONArray logsArray = new JSONArray(existingLogs);
            logsArray.put(logEntry);
            sharedPreferences.edit().putString(KEY_PENDING_LOGS, logsArray.toString()).apply();
        } catch (JSONException e) {
            Log.e(TAG, "Error storing log locally", e);
        }
    }

    /**
     * Sync all pending logs with server
     */
    public void syncWithServer() {
        String pendingLogs = sharedPreferences.getString(KEY_PENDING_LOGS, "[]");

        try {
            JSONArray logsArray = new JSONArray(pendingLogs);
            if (logsArray.length() == 0) {
                Log.d(TAG, "No pending logs to sync");
                return;
            }

            // Send to server
            sendLogsToServer(logsArray);

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing pending logs", e);
        }
    }

    /**
     * Send logs to server
     */
    private void sendLogsToServer(JSONArray logsArray) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("deviceId", deviceId);
            payload.put("logs", logsArray);

            RequestBody body = RequestBody.create(JSON, payload.toString());
            Request request = new Request.Builder()
                .url(SERVER_URL)
                .post(body)
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "Failed to sync logs", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Clear pending logs on successful sync
                        sharedPreferences.edit().putString(KEY_PENDING_LOGS, "[]").apply();
                        Log.d(TAG, "Logs synced successfully: " + logsArray.length() + " entries");
                    } else {
                        Log.e(TAG, "Server returned error: " + response.code());
                    }
                    response.close();
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creating payload", e);
        }
    }

    /**
     * Force sync with server (call when internet connection is available)
     */
    public void forceSync() {
        syncWithServer();
    }

    /**
     * Get today's date as string
     */
    private String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Get pending logs count
     */
    public int getPendingLogsCount() {
        try {
            String pendingLogs = sharedPreferences.getString(KEY_PENDING_LOGS, "[]");
            JSONArray logsArray = new JSONArray(pendingLogs);
            return logsArray.length();
        } catch (JSONException e) {
            Log.e(TAG, "Error getting pending logs count", e);
            return 0;
        }
    }
    // Recommended solution: Use a locally generated, persistent UUID.
    private String getOrCreateDeviceId() {
        String deviceId = sharedPreferences.getString(KEY_DEVICE_ID, null);
        if (deviceId == null) {
            // Generate a new unique ID for this app install
            deviceId = "UUID_" + UUID.randomUUID().toString();
            sharedPreferences.edit().putString(KEY_DEVICE_ID, deviceId).apply();
            saveDeviceInfo();
        }
        return deviceId;
    }
    /**
     * Get all stored logs (for debugging)
     */
    public List<JSONObject> getStoredLogs() {
        List<JSONObject> logs = new ArrayList<>();
        try {
            String pendingLogs = sharedPreferences.getString(KEY_PENDING_LOGS, "[]");
            JSONArray logsArray = new JSONArray(pendingLogs);
            for (int i = 0; i < logsArray.length(); i++) {
                logs.add(logsArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error getting stored logs", e);
        }
        return logs;
    }

    /**
     * Clear all stored logs (use with caution)
     */
    public void clearStoredLogs() {
        sharedPreferences.edit().putString(KEY_PENDING_LOGS, "[]").apply();
    }
}
