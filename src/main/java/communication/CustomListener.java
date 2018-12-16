package communication;

import io.socket.emitter.Emitter;

    public class CustomListener<T> implements Emitter.Listener
    {

        Class<T> typeClass;

        public CustomListener(Class<T> typeClass)
        {
            this.typeClass=typeClass;
        }

        public void call(Object... args) {

            // onData((T)Utils.gson.fromJson(args[0].toString(),typeClass));
        }


    }


