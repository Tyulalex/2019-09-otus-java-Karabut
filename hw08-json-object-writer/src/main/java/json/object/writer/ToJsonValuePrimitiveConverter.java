package json.object.writer;

import javax.json.Json;
import javax.json.JsonValue;

public class ToJsonValuePrimitiveConverter {


    static boolean canConvert(Class clazz) {
        return clazz.isPrimitive() || isInteger(clazz) ||
                isLong(clazz) || isShort(clazz) ||
                isByte(clazz) || isDouble(clazz) || isFloat(clazz) ||
                isString(clazz) || isChar(clazz) || isBoolean(clazz);
    }


    static JsonValue convert(Class clazz, Object object) {
        if (isInteger(clazz)) {
            return Json.createValue((int) object);

        } else if (isBoolean(clazz)) {
            return (boolean) object ? JsonValue.TRUE : JsonValue.FALSE;

        } else if (isLong(clazz)) {
            return Json.createValue((long) object);

        } else if (isShort(clazz)) {
            return Json.createValue((short) object);

        } else if (isByte(clazz)) {
            return Json.createValue((byte) object);

        } else if (isDouble(clazz)) {
            return Json.createValue((double) object);

        } else if (isFloat(clazz)) {
            return Json.createValue(Double.parseDouble(String.valueOf((float) object)));

        } else if (isChar(clazz)) {
            return Json.createValue(Character.toString((char) object));

        } else if (isString(clazz)) {
            return Json.createValue((String) object);

        } else {
            throw new UnsupportedOperationException(String.format("Unsupported type %s", clazz));
        }
    }

    private static boolean isBoolean(Class clazz) {
        return clazz.isAssignableFrom(boolean.class) || clazz.isAssignableFrom(Boolean.class);
    }

    private static boolean isInteger(Class clazz) {
        return clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class);
    }

    private static boolean isLong(Class clazz) {
        return clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class);
    }

    private static boolean isShort(Class clazz) {
        return clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class);
    }

    private static boolean isByte(Class clazz) {
        return clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class);
    }

    private static boolean isDouble(Class clazz) {
        return clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class);
    }

    private static boolean isFloat(Class clazz) {
        return clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class);
    }

    private static boolean isChar(Class clazz) {
        return clazz.isAssignableFrom(char.class) || clazz.isAssignableFrom(Character.class);
    }

    private static boolean isString(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }
}
