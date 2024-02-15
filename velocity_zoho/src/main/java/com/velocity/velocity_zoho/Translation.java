package com.velocity.velocity_zoho;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Translation {
    private String text;
    private String to;

    // Getter for 'text'
    public String getText() {
        return text;
    }

    // Setter for 'text'
    public void setText(String text) {
        this.text = text;
    }

    // Getter for 'to'
    public String getTo() {
        return to;
    }

    // Setter for 'to'
    public void setTo(String to) {
        this.to = to;
    }

    public static List<Translation> loadTranslationsFromRaw(Context context, int resourceId) {
        List<Translation> translations = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int byteRead = inputStream.read(buffer);
            inputStream.close();
            if (byteRead > 0) {
                String json = new String(buffer, StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Translation translation = new Translation();
                    translation.setText(jsonObject.getString("text"));
                    translation.setTo(jsonObject.getString("to"));
                    translations.add(translation);
                }
            }
        } catch (Exception e) {
            // Handle the exception
        }
        return translations;
    }
}
