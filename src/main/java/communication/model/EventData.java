package communication.model;

public class EventData {
    public String clientId;
    String data;
    public Long timeStamp;
    public EventData()
    {
    //    super();
    }
    public EventData(String clientId, String data)
    {
      //  super();
        this.clientId=clientId;
        this.data=data;
        this.timeStamp=System.currentTimeMillis();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
