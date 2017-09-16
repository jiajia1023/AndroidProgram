package com.mylibrary.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by chris zou on 2016/7/29.
 */
public class SharedPreferencesHelper {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferencesHelper(Context context, String keyName) {
        mSharedPreferences = context.getSharedPreferences(keyName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public void putValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void putValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void putValue(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void putValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void putValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void putValue(String key, Set<String> value) {
        mEditor.putStringSet(key, value);
        mEditor.commit();
    }

    public <T> T getValue(String key, T defaultValue) {
        Map<String, ?> map = getAll();
        return map.get(key)==null?defaultValue: (T) map.get(key);
    }

    public void clearAll(){
        getEditor().clear();
        getEditor().commit();
    }

    public boolean removeValue(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }

    public Set<String> getSet(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }


    public static class Builder {

        private static Builder instance;
        private static SharedPreferencesHelper helper;

        private static class ClassHolder {
            public static final Builder INSTANCE = new Builder();
        }

        public static Builder getInstance(Context context, String keyName) {
            if (instance == null) {
                instance = ClassHolder.INSTANCE;
            }
            if (helper == null) {
                helper = new SharedPreferencesHelper(context, keyName);
            }
            return instance;
        }

        public Builder putValue(String key, String value) {
            helper.putValue(key, value);
            return this;
        }

        public Builder putValue(String key, int value) {
            helper.putValue(key, value);
            return this;
        }

        public Builder putValue(String key, long value) {
            helper.putValue(key, value);
            return this;
        }

        public Builder putValue(String key, boolean value) {
            helper.putValue(key, value);
            return this;
        }

        public Builder putValue(String key, float value) {
            helper.putValue(key, value);
            return this;
        }

        public Builder putValue(String key, Set<String> value) {
            helper.putValue(key, value);
            return this;
        }

        public SharedPreferencesHelper getHelper() {
            return helper;
        }
    }
}
