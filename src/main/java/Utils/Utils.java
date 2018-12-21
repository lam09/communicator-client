package Utils;


import com.google.gson.Gson;
import communication.model.EventData;

public class Utils {
    public static Gson gson = new Gson();
 /*   public static Object fromJson(String str,Class<?> clazz)
    {
        return gson.fromJson(str,clazz.getClass());
    }*/
    public static String toJson(Object o)
    {
        return gson.toJson(o, EventData.class);
    }
    public  static <T> T fromJson(String json,Class<T> type) {
        return Utils.gson.fromJson(json, type);
    }
}
