package sg.edu.nus.iss.phoenix.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sg.edu.nus.iss.phoenix.core.android.controller.entity.RadioProgram;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.Role;
import sg.edu.nus.iss.phoenix.core.android.controller.entity.User;
import sg.edu.nus.iss.phoenix.schedule.android.entity.ProgramSlot;

public class JSONHelper {
    private static final String TAG = JSONHelper.class.getName();

    public static JSONObject toJSON(ProgramSlot programSlot) {
        JSONObject result = new JSONObject();
        if (programSlot != null) {
            try {
                result.put("radioProgram", toJSON(programSlot.getRadioProgram()));
                result.put("presenter", toJSON(programSlot.getPresenter()));
                result.put("producer", toJSON(programSlot.getProducer()));
                result.put("dateOfProgram", programSlot.getDateOfProgram().toString());
                result.put("duration", programSlot.getDuration().toString());
                result.put("assignedBy", programSlot.getAssignedBy().toString());
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        }
        return result;
    }

    public static JSONObject toJSON(RadioProgram radioProgram) {
        JSONObject result = new JSONObject();
        if (radioProgram != null) {
            try {
                result.put("name", radioProgram.getRadioProgramName());
                result.put("description", radioProgram.getRadioProgramDescription());
                result.put("typicalDuration", radioProgram.getRadioProgramDuration());
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        }
        return result;
    }

    public static JSONObject toJSON(User user) {
        JSONObject result = new JSONObject();
        if (user != null) {
            try {
                result.put("id", user.getId());
                result.put("name", user.getUserName());
                result.put("password", user.getUserPassword());
                result.put("roles", toJSON(user.getRoles()));
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        }
        return result;
    }

    public static JSONArray toJSON(List<Role> roles) {
        JSONArray result = new JSONArray();
        if (roles != null) {
            for (Role role : roles) {
                if (role != null) {
                    result.put(toJSON(role));
                }
            }
        }
        return result;
    }

    public static JSONObject toJSON(Role role) {
        JSONObject result = new JSONObject();
        if (role != null) {
            try {
                result.put("role", role.getRole());
                result.put("accessPrivilege", role.getAccessPrivilege());
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        }
        return result;
    }
}
