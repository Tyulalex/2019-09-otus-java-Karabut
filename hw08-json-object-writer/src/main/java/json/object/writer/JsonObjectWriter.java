package json.object.writer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;

public class JsonObjectWriter {

    private JavaXJsonObjectBuilder javaXJsonObjectBuilder;

    public JsonObjectWriter() {
        this.javaXJsonObjectBuilder = new JavaXJsonObjectBuilder();
    }

    public String toJson(Object objectToJson) throws IllegalAccessException {
        JsonObject jsonObject = javaXJsonObjectBuilder.buildJsonObject(objectToJson);
        return getJsonAsString(jsonObject);
    }

    private String getJsonAsString(JsonObject jsonObject) {
        try (StringWriter stringWriter = new StringWriter();
             JsonWriter jsonWriter = Json.createWriter(stringWriter)) {

            jsonWriter.writeObject(jsonObject);
            return stringWriter.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
