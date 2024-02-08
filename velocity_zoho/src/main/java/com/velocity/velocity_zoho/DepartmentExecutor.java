package com.velocity.velocity_zoho;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

public class DepartmentExecutor {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface DepartmentCallback {
        void onDepartmentsLoaded(JSONArray departments);
        void onError(Throwable error);
    }

    public void loadDepartments(DepartmentCallback callback) {
        Future<JSONArray> future = executorService.submit(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String urlString = "https://content.icas.health/assets/zohoDepartments.json";
                Request request = new Request.Builder()
                        .url(urlString)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    return new JSONArray(responseData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            JSONArray departments = future.get();
            callback.onDepartmentsLoaded(departments);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}