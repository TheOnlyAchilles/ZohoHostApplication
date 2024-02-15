package com.velocity.velocity_zoho;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private List<String> codes;
    private boolean defaults;

    // Getter for 'name'
    public String getName() {
        return name;
    }

    // Setter for 'name'
    public void setName(String name) {
        this.name = name;
    }

    // Getter for 'codes'
    public List<String> getCodes() {
        return codes;
    }

    // Setter for 'codes'
    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    // Getter for 'defaults'
    public boolean getDefaults() {
        return defaults;
    }

    // Setter for 'defaults'
    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
    }

    public static List<Department> loadDepartmentFromRaw(JSONArray departmentJSON) {
        List<Department> departments = new ArrayList<>();
        try {
            for (int i = 0; i < departmentJSON.length(); i++) {
                JSONObject jsonObject = departmentJSON.getJSONObject(i);
                Department department = new Department();
                department.setName(jsonObject.getString("name"));
                JSONArray codesJsonArray = jsonObject.getJSONArray("codes");
                List<String> codes = new ArrayList<>();
                for (int j = 0; j < codesJsonArray.length(); j++) {
                    codes.add(codesJsonArray.getString(j));
                }
                department.setCodes(codes);
                department.setDefaults(jsonObject.getBoolean("default"));
                departments.add(department);
            }
        } catch (Exception e) {
            // Handle the exception
        }
        return departments;
    }
}