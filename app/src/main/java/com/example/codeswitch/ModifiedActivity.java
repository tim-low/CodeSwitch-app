package com.example.codeswitch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import com.example.codeswitch.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class ModifiedActivity extends Activity {

    public String getTextInputEditText(int editTextId)
    {
        TextInputEditText editText = (TextInputEditText) findViewById(editTextId);
        return editText.getText().toString();

    }

    public String getEditText(int editTextId)
    {
        EditText editText = (EditText) findViewById(editTextId);
        return editText.getText().toString();
    }

    public void printEditText(int editTextId, String message)
    {
        EditText editText = (EditText) findViewById(editTextId);
        editText.setText(message);
    }

    public void printPrompt (int textId, String message)
    {
        TextView tv1 = (TextView) findViewById(textId);
        tv1.setText(message);
    }

    public void saveUserToPrefs(User user) {
        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("CurrentUser", json);
        prefsEditor.commit();
    }

    public User getUserFromPrefs() {
        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String json = mPrefs.getString("CurrentUser", null);
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }
}

