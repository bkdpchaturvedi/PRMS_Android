package sg.edu.nus.iss.phoenix.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.restful.Error;
import sg.edu.nus.iss.phoenix.restful.JSONEnvelop;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.user.android.delegate.RetrieveUsersDelegate;

public class JSONEnvelopHelper {

    private static final String TAG = RetrieveUsersDelegate.class.getName();


    public static JSONEnvelop<Boolean>  parseEnvelopBoolean(String response) {
        JSONEnvelop<Boolean> result = new JSONEnvelop<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")) {
                result.setData(jsonObject.getBoolean("data"));
            }
            if (jsonObject.has("error")) {
                result.setError(parseError(jsonObject.getJSONObject("error")));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            result.setError(new Error("", e.getMessage()));
        }
        return result;
    }

    public static JSONEnvelop<ProgramSlot> parseEnvelopProgramSlot(String response) {
        JSONEnvelop<ProgramSlot> result = new JSONEnvelop<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")) {
                result.setData(parseProgramSlot(jsonObject.getJSONObject("data")));
            }
            if (jsonObject.has("error")) {
                result.setError(parseError(jsonObject.getJSONObject("error")));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public static JSONEnvelop<List<ProgramSlot>> parseEnvelopProgramSlots(String response) {
        JSONEnvelop<List<ProgramSlot>> result = new JSONEnvelop<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")) {
                result.setData(parseProgramSlots(jsonObject.getJSONArray("data")));
            }
            if (jsonObject.has("error")) {
                result.setError(parseError(jsonObject.getJSONObject("error")));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public static JSONEnvelop<User> parseEnvelopUser(String response) {
        JSONEnvelop<User> result = new JSONEnvelop<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")) {
                result.setData(parseUser(jsonObject.getJSONObject("data")));
            }
            if (jsonObject.has("error")) {
                result.setError(parseError(jsonObject.getJSONObject("error")));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    public static JSONEnvelop<List<User>> parseEnvelopUsers(String response) {
        JSONEnvelop<List<User>> result = new JSONEnvelop<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")) {
                result.setData(parseUsers(jsonObject.getJSONArray("data")));
            }
            if (jsonObject.has("error")) {
                result.setError(parseError(jsonObject.getJSONObject("error")));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    private static List<RadioProgram> parseRadioPrograms(JSONArray jsonArray) {
        List<RadioProgram> result = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    result.add(parseRadioProgram(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            Log.v(TAG, "Empty JSON Array.");
        }
        return result;
    }

    private static RadioProgram parseRadioProgram(JSONObject jsonObject) {
        RadioProgram result = null;
        if (jsonObject != null) {
            try {
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String typicalDuration = jsonObject.getString("typicalDuration");
                result = new RadioProgram(name, description, typicalDuration);
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "Empty JSON Object.");
        }
        return result;
    }

    private static List<ProgramSlot> parseProgramSlots(JSONArray jsonArray) {
        List<ProgramSlot> result = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    result.add(parseProgramSlot(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            Log.v(TAG, "Empty JSON Array.");
        }
        return result;
    }

    private static ProgramSlot parseProgramSlot(JSONObject jsonObject) {
        ProgramSlot result = null;
        if (jsonObject != null) {
            try {
                RadioProgram radioProgram = parseRadioProgram(jsonObject.getJSONObject("radioProgram"));
                User presenter = parseUser(jsonObject.getJSONObject("presenter"));
                User producer = parseUser(jsonObject.getJSONObject("producer"));
                ZonedDateTime dateOfProgram = ZonedDateTime.parse(jsonObject.getString("dateOfProgram")).withZoneSameInstant(ZoneId.systemDefault());
                Duration duration = Duration.parse(jsonObject.getString("duration"));
                String assignedBy = jsonObject.getString("assignedBy");
                result = new ProgramSlot(dateOfProgram, duration, radioProgram, presenter, producer, assignedBy);
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "Empty JSON Object.");
        }
        return result;
    }

    private static List<User> parseUsers(JSONArray jsonArray) {
        List<User> result = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    result.add(parseUser(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            Log.v(TAG, "Empty JSON Array.");
        }
        return result;
    }

    private static User parseUser(JSONObject jsonObject) {
        User result = null;
        if (jsonObject != null) {
            try {
                String password = jsonObject.getString("password");
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                List<Role> roles = parseRoles (jsonObject.getJSONArray("roles"));
                result = new User(id, name, password,roles);
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "Empty JSON Object.");
        }
        return result;
    }

    private static List<Role> parseRoles(JSONArray jsonArray) {
        List<Role> result = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    result.add(parseRole(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            Log.v(TAG, "Empty JSON Array.");
        }
        return result;
    }

    private static Role parseRole(JSONObject jsonObject) {
        Role result = null;
        if (jsonObject != null) {
            try {
                String role = jsonObject.getString("role");
                String accessPrivilege = jsonObject.getString("accessPrivilege");
                result = new Role(role, accessPrivilege);
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "Empty JSON Object.");
        }
        return result;
    }


    private static Error parseError(JSONObject jsonObject) {
        Error result = null;
        if (jsonObject != null) {
            try {
                String err = jsonObject.getString("error");
                String description = jsonObject.getString("description");
                result = new Error(err, description);

            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        } else {
            Log.v(TAG, "JSON response error.");
        }
        return result;
    }

}
