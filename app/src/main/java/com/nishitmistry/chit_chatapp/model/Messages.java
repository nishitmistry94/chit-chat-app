package com.nishitmistry.chit_chatapp.model;
public class Messages {
    String message;
    String senderUid;
    String timeStamp;
//    ReadRecipt readRecipt;
    Messages()
    {
    }
    public Messages(String senderUid, String message, String timeStamp)
    {
        this.message=message;
        this.senderUid=senderUid;
        this.timeStamp=timeStamp;
//        this.readRecipt=readRecipt;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderUid() {
        return senderUid;
    }
}
