package Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class JsonDecoder implements Decoder.Text<JSONObject> {

    @Override
    public JSONObject decode(String s) {
        return new JSONObject(s);
    }

    @Override
    public boolean willDecode(String s) {
        // check if string is json
        try {
            new JSONObject(s);
        } catch (JSONException ex) {
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(s);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}