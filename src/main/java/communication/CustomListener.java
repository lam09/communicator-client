package communication;

import Utils.Utils;
import io.socket.emitter.Emitter;

public class CustomListener<T> implements Emitter.Listener
    {

        Class<T> typeClass;

        public CustomListener(Class<T> typeClass)
        {
            this.typeClass=typeClass;
        }

        public void call(Object... args) {
            //JSONObject jsonObject=args[0];
            System.out.println("recieved " + args[0].toString());
             onData( fromJson(args[0].toString(),typeClass));
        }

        public void onData(T object)
        {
        }

        public  static <T> T fromJson(String json,Class<T> type) {
            return Utils.gson.fromJson(json, type);
        }
    }


