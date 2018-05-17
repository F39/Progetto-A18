package Utils;

import org.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<JSONObject> {

    @Override
    public String encode(JSONObject message) throws EncodeException {
        return message.toString();
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