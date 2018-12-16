package communication;

public class Main  {

    public static void main(String[] args) {
       // Communicator communicator = new Communicator();
      //  communicator.createSocket();
        RestRequest request=new RestRequest();
     /*   new Runnable() {
            public void run() {
                while(true)
                {
                    System.out.println("hello");
                    try {
                        sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.run();*/
        request.sendRequest("http://localhost:12001/admin/getMenu");
    }
}
