package com.velocity.velocity_zoho;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.VisitorChat;
import com.zoho.livechat.android.ZohoLiveChat;
import com.zoho.livechat.android.constants.ConversationType;
import com.zoho.livechat.android.listeners.ConversationListener;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VelocityZohoChat extends Application {
    private String appKey;
    private String accessKey;
    private Application application;

    public VelocityZohoChat() {
    }

    public VelocityZohoChat(String appKey, String accessKey, Application application) {
        this.appKey = appKey;
        this.accessKey = accessKey;
        this.application = application;
        this.initZoho();
    }

    public void initZoho() {
        try {
            InitConfig initConfig = new InitConfig();
            ZohoSalesIQ.init(application, appKey, accessKey, initConfig, new InitListener() {
                @Override
                public void onInitSuccess() {
                    Log.i("LOGS", "SUCCESS");
                }

                @Override
                public void onInitError(int errorCode, String errorMessage) {
                    Log.e("LOGS", "ERROR: " + errorMessage);
                }
            });
        } catch (Exception e) {
            Log.e("LOGS", "CATCH: " + e.getMessage());
        }
    }

    public void setDepartment(String countryCode, boolean testMode, String environment) {
        DepartmentExecutor departmentExecutor = new DepartmentExecutor();
        departmentExecutor.loadDepartments(new DepartmentExecutor.DepartmentCallback() {
            @Override
            public void onDepartmentsLoaded(JSONArray departmentJSON) {
                String departmentName = "";
                List<Department> departments = Department.loadDepartmentFromRaw(departmentJSON);
                if (!environment.equalsIgnoreCase("staging")) {
                    if (!testMode) {
                        for (Department department : departments) {
                            boolean isMatched = false;
                            for (String code : department.getCodes()) {

                                if (code.equalsIgnoreCase(countryCode)) {
                                    departmentName = department.getName();
                                    ZohoSalesIQ.Chat.setDepartment(departmentName);
                                    isMatched = true;
                                    break;
                                }
                            }
                            if (!isMatched && department.getDefaults()) {
                                departmentName = department.getName();
                                ZohoSalesIQ.Chat.setDepartment(departmentName);
                                break;
                            }
                            if (!departmentName.isEmpty()) {
                                break;
                            }
                        }
                    }
                }
                Log.d("LOGS", "onDepartmentsLoaded: " + departmentName);
            }

            @Override
            public void onError(Throwable error) {
                // Handle the error (show error message, etc.)
                Log.e("LOGS", "Error fetching departments: " + error.getMessage());
            }
        });
    }

    public void startChat(String companyName, @Nullable String serviceName, String title, Activity activity) {
        setAdditionalInformation(companyName, serviceName);
        setTitle(title);
        open(activity);
    }

    public void openChat(String languageCode, String countryCode, boolean testMode, String environment, Context context) {
        ZohoLiveChat.Visitor.setQuestion(questionText(languageCode, context));
        setDepartment(countryCode, testMode, environment);
        setLanguage(languageCode);
        ZohoSalesIQ.Chat.show();
        Log.d("LOGS", "Chat Shown");
    }

    private String questionText(String languageCode, Context context) {
        String questionText = "";
        List<Translation> questions = Translation.loadTranslationsFromRaw(context, R.raw.translations_questions);
        for (Translation translation : questions) {
            if (translation.getTo().equalsIgnoreCase(languageCode)) {
                questionText = translation.getText();
                break;
            }
        }
        return questionText;
    }

    private String nameText(String languageCode, Context context) {
        List<Translation> nameTranslations = Translation.loadTranslationsFromRaw(context, R.raw.translations_name);
        String nameText = "";
        for (Translation translation : nameTranslations) {
            if (translation.getTo().equalsIgnoreCase(languageCode)) {
                nameText = translation.getText();
                break;

            }
        }
        return nameText;
    }

    private String emailText(String languageCode, Context context) {
        List<Translation> emailTranslations = Translation.loadTranslationsFromRaw(context, R.raw.translations_email);
        String emailText = "";
        for (Translation translation : emailTranslations) {
            if (translation.getTo().equalsIgnoreCase(languageCode)) {
                emailText = translation.getText();
                break;
            }
        }
        return emailText;
    }

    private void setLanguage(String languageCode) {
        ZohoSalesIQ.Chat.setLanguage(languageCode);
    }

    private void setTitle(String title) {
        ZohoSalesIQ.Tracking.setPageTitle(title);
        ZohoSalesIQ.Chat.setTitle(title);
    }

    public static void setAdditionalInformation(String companyName, @Nullable String serviceName) {
        Map<String, Object> result = new HashMap<>();
        result.put("Company Name", companyName);

        if (serviceName != null) {
            result.put("Page", "Services");
            result.put("Primary Need", serviceName);
            result.put("Potential Risk", "No");
        } else {
            result.put("Page", "Support");
            result.put("Primary Need", "None");
            result.put("Potential Risk", "Yes");
        }

        for (Map.Entry<String, Object> entry : result.entrySet()) {
            String currentDynamicKey = entry.getKey();
            Object currentDynamicValue = entry.getValue();
            Log.d("LOGS", "KEY: " + currentDynamicKey + "VALUE: " + currentDynamicValue);
            ZohoSalesIQ.Visitor.addInfo(currentDynamicKey, currentDynamicValue.toString());
        }
    }

    private void open(Activity activity) {
        ZohoSalesIQ.Chat.getList(
                ConversationType.OPEN,
                new ConversationListener() {
                    @Override
                    public void onSuccess(ArrayList<VisitorChat> chats) {
                        if (removeWaitingMissed(chats).size() > 0) {
                            ZohoSalesIQ.Chat.open(activity);
                            Log.d("LOGS", "Chats Opened");
                        } else {
                            ZohoSalesIQ.Chat.open(activity);
                            Log.d("LOGS", "Chat Opened");
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        //your code
                        Log.d("LOGS", "onFailure: " + code + " : " + message);
                    }
                }
        );
    }

    private ArrayList<VisitorChat> removeWaitingMissed(ArrayList<VisitorChat> chats) {
        ListIterator<VisitorChat> data = chats.listIterator();
        ArrayList<VisitorChat> filteredChats = new ArrayList<>();
        while (data.hasNext()) {
            VisitorChat currentChat = data.next();
            if (currentChat.getAttenderId() != null && currentChat.getAttenderId().compareTo("") == 0 && currentChat.getChatStatus().compareTo("WAITING") == 0) {
                continue;
            }
            filteredChats.add(currentChat);
        }
        return filteredChats;
    }

    private void endChat() {
        ZohoSalesIQ.Chat.getList(
                ConversationType.OPEN,
                new ConversationListener() {
                    @Override
                    public void onSuccess(ArrayList<VisitorChat> chats) {
                        ZohoSalesIQ.Chat.endChat(chats.get(0).getChatID());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        //your code
                        Log.d("LOGS", "onFailure: " + code + " : " + message);
                    }
                }
        );
    }

    public void showZohoLauncher() {
        ZohoSalesIQ.Chat.getList(
                ConversationType.OPEN,
                new ConversationListener() {
                    @Override
                    public void onSuccess(ArrayList<VisitorChat> chats) {
                        if (removeWaitingMissed(chats).size() > 0) {
                            ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS);
                        } else {
                            ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.NEVER);
                        }
                        if (chats.size() > 0) {
                            checkAndEndChat(chats.get(0).getLastMessage().getTime());
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        //your code
                    }
                }
        );
    }

    private void checkAndEndChat(long inputUnixDate) {
        long currentMillis = System.currentTimeMillis();
        long hoursDifference = (currentMillis - inputUnixDate);
        long hours = TimeUnit.MILLISECONDS.toHours(hoursDifference);
        if (hours > 24) {
            endSession();
        }
    }

    private void endSession() {
        endChat();
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.WHEN_ACTIVE_CHAT);
        ZohoSalesIQ.clearData(application);
    }
}