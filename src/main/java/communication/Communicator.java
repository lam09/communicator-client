package communication;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.util.logging.Logger;

public class Communicator {
    Socket socketio;
    boolean connected=false;
    public Communicator()
    {

    }
    public boolean createSocket()
    {
        try {
            socketio = IO.socket("http://localhost:13001");

            if(socketio!=null)
            {
                System.out.println("created socket io");
                socketio.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    public void call(Object... args) {
                        connected=true;
                        System.out.println("connected");
                    }

                }).on("event", new Emitter.Listener() {

                    public void call(Object... args) {}

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    public void call(Object... args) {
                        System.out.println("disconnected");

                        connected=false;
                    }

                });
                socketio.connect();

            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    Listener connectListener = new Emitter.Listener() {

        public void call(Object... args) {
            JSONObject obj = (JSONObject)args[0];
        }

    };

    CustomListener<EventData> handleGetSymbols = new CustomListener<EventData>(EventData.class) {
        public void onData(EventData data) {

        }
    };



}
