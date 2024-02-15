package com.velocity.velocity_zoho;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.VisitorChat;
import com.zoho.livechat.android.ZohoLiveChat;
import com.zoho.livechat.android.constants.ConversationType;
import com.zoho.livechat.android.listeners.ConversationListener;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class velocity_zoho_chat extends Application {
    private String appKey;
    private String accessKey;
    private Application application;

    public velocity_zoho_chat() {
    }

    public velocity_zoho_chat(String appKey, String accessKey, Application application) {
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
                    showZohoLauncher();
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

    public void setDepartment(String countryCode) {
        DepartmentExecutor departmentExecutor = new DepartmentExecutor();
        departmentExecutor.loadDepartments(new DepartmentExecutor.DepartmentCallback() {
            @Override
            public void onDepartmentsLoaded(JSONArray departmentJSON) {
                // Handle the loaded departments (update UI, etc.)
                String departmentName = "";
                List<Department> departments = Department.loadDepartmentFromRaw(departmentJSON);
                for (Department department : departments) {
                    for (String code : department.getCodes()) {
                        if (code.equalsIgnoreCase(countryCode)) {
                            departmentName = department.getName();
                            break;
                        }
                    }
                    if (!departmentName.isEmpty()) {
                        break;
                    }
                }
                Log.d("LOGS", "onDepartmentsLoaded: " + departmentName);
                ZohoSalesIQ.Chat.setDepartment(departmentName);
            }
            @Override
            public void onError(Throwable error) {
                // Handle the error (show error message, etc.)
                Log.e("LOGS", "Error fetching departments: " + error.getMessage());
            }
        });
    }

    public void startChat(Map<String, String> additionalInformation, String title, Activity activity) {
        setAdditionalInformation(additionalInformation);
        setTitle(title);
        open(activity);
    }

    public void openChat(String countryCode, String languageCode, Context context) {
        ZohoLiveChat.Visitor.setQuestion(questionText(languageCode, context));
        setDepartment(countryCode);
        setLanguage(languageCode);
        showZohoLauncher();
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

    public static void setAdditionalInformation(Map<String, String> additionalInformation) {
        for (Map.Entry<String, String> entry : additionalInformation.entrySet()) {
            String currentDynamicKey = entry.getKey();
            String currentDynamicValue = entry.getValue();
            Log.d("LOGS", "Setting " + currentDynamicKey + " to " + currentDynamicValue);
            ZohoSalesIQ.Visitor.addInfo(currentDynamicKey, currentDynamicValue);
        }
    }

    private void open(Activity activity) {
        ZohoSalesIQ.Chat.getList(
                ConversationType.OPEN,
                new ConversationListener() {
                    @Override
                    public void onSuccess(ArrayList<VisitorChat> chats) {
                        if (removeWaitingMissed(chats).size() > 0){
                            ZohoSalesIQ.Chat.open(activity);
                            Log.d("LOGS", "Chats Opened");
                        }
                        else
                        {
                            ZohoSalesIQ.Chat.open(activity);
                            Log.d("LOGS", "Chat Opened");
                        }
                    }
                    @Override
                    public void onFailure(int code, String message) {
                        //your code
                        Log.d("LOGS", "onFailure: " + code + " : "+ message);
                    }
                }
        );
    }

    private ArrayList<VisitorChat> removeWaitingMissed(ArrayList<VisitorChat> chats)
    {
        ListIterator<VisitorChat> data = chats.listIterator();
        ArrayList<VisitorChat> filteredChats = new ArrayList<VisitorChat>();
        while(data.hasNext()) {
            VisitorChat currentChat = data.next();
            if (currentChat.getAttenderId() != null && currentChat.getAttenderId().compareTo("") == 0 && currentChat.getChatStatus().compareTo("WAITING") == 0)
            {
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
                        Log.d("LOGS", "onFailure: " + code + " : "+ message);
                    }
                }
        );
    }

    private void showZohoLauncher() {
        ZohoSalesIQ.Chat.getList(
                ConversationType.OPEN,
                new ConversationListener() {
                    @Override
                    public void onSuccess(ArrayList<VisitorChat> chats) {
                        ZohoSalesIQ.showLauncher(removeWaitingMissed(chats).size() > 0);
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

    private void endSession()  {
        endChat();
        ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.WHEN_ACTIVE_CHAT);
        ZohoSalesIQ.clearData(application);
    }
}