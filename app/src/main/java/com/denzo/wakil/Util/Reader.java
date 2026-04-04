package com.denzo.wakil.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* THIS CLASS PROVIDE METHODS FOR READING JSON FILES FROM ASSETS AS WELL AS FROM SHARED PREFERENCES
AND RETURNS LIST OF SUITABLE OBJECTS.*/
public class Reader {
    public static String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<Hotel> getRestaurantList(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        String json = loadJSONFromAsset(context, "hotels.json");
        try {
            return Arrays.asList(mapper.readValue(json, Hotel[].class));
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static User getUserList(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        String json = loadJSONFromAsset(context, "users.json");
        try {
            return (mapper.readValue(json, User.class));
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static List<UserModel> getRegisteredUsers(Context context) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String json = pref.getString("registered_users", null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<UserModel>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static ArrayList<Bookings> getBookingsList(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String json = pref.getString("bookings", "xyz");
        if (json.equals("xyz")) return null;
        else {
            try {
                List<Bookings> bookinglist = Arrays.asList(mapper.readValue(json, Bookings[].class));
                return new ArrayList<>(bookinglist);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public static ArrayList<Drafts> getDraftsList(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
        String json = pref.getString("drafts", "xyz");
        if (json.equals("xyz")) return null;
        else {
            try {
                List<Drafts> draftlist = Arrays.asList(mapper.readValue(json, Drafts[].class));
                return new ArrayList<>(draftlist);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }
}
