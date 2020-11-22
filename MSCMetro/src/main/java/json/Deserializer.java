package json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class Deserializer implements JsonDeserializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject lines = json.getAsJsonObject().get("stations").getAsJsonObject();
        StringBuilder print = new StringBuilder();

        for (String k : lines.keySet()) {
            JsonArray stations = lines.get(k).getAsJsonArray();
            print.append("Line ").append(k).append(": ").append(stations.size()).append(" stations\n");
//            System.out.println("Линия " + k + " - Количество станций: " + stations.size() + ";");
        }
        return print.toString();
    }
}