package com.example.ui;

import org.json.JSONObject;

public interface DataLinker {
    public void treatData(JSONObject Data);
    public String checkIfConflictsWithDataset(JSONObject Data);
}
