package json.object.writer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

class JavaXJsonObjectBuilder {

    JsonObject buildJsonObject(Object objectToJson) throws IllegalAccessException {
        var jsonObjectBuilder = Json.createObjectBuilder();
        Class classOfObject = objectToJson.getClass();
        Field[] fields = classOfObject.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Class fieldType = field.getType();
            String fieldName = field.getName();
            Object obj = field.get(objectToJson);
            if (obj == null) {
                return jsonObjectBuilder.build();
            } else if (fieldType.isArray()) {
                jsonObjectBuilder.add(fieldName, this.buildJsonArray(obj));

            } else if (Collection.class.isAssignableFrom(fieldType)) {
                jsonObjectBuilder.add(fieldName, this.buildJsonArray(((Collection) obj).toArray()));

            } else if (ToJsonValuePrimitiveConverter.canConvert(fieldType)) {
                JsonValue jsonValue = ToJsonValuePrimitiveConverter.convert(fieldType, obj);
                jsonObjectBuilder.add(fieldName, jsonValue);

            } else if (Map.class.isAssignableFrom(fieldType)) {
                jsonObjectBuilder.add(fieldName, buildJsonMap(obj));

            } else {
                jsonObjectBuilder.add(fieldName, buildJsonObject(obj));
            }

        }
        return jsonObjectBuilder.build();
    }


    private JsonValue buildJsonValue(Object object) throws IllegalAccessException {
        Class clazz = object.getClass();
        if (ToJsonValuePrimitiveConverter.canConvert(clazz)) {
            return ToJsonValuePrimitiveConverter.convert(clazz, object);
        } else {
            return this.buildJsonObject(object);
        }
    }

    private boolean isCollectionOrArray(Class clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    private JsonArray buildJsonArray(Object object) throws IllegalAccessException {
        if (!isCollectionOrArray(object.getClass())) {
            throw new UnsupportedOperationException(String.format("unable to build Json Array for %s",
                    object.getClass()));
        }
        var jsonArrayBuilder = Json.createArrayBuilder();
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(object, i);
            JsonValue jsonValue = this.buildJsonValue(element);
            jsonArrayBuilder.add(jsonValue);

        }
        return jsonArrayBuilder.build();
    }

    private JsonObject buildJsonMap(Object object) {
        var jsonBuilder = Json.createObjectBuilder();
        if (object != null) {
            Set<Map.Entry<String, Object>> entrySet = ((Map) object).entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                jsonBuilder.add(entry.getKey(), ToJsonValuePrimitiveConverter.convert(entry.getValue().getClass(),
                        entry.getValue()));
            }
        }
        return jsonBuilder.build();
    }
}
