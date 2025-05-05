package com.noah;

public class Message {
    public String username;
    public String date;
    public String content;

    public Message(String user, String time, String msg) {
        username = user;
        date = time;
        content = msg;
    }

    public String toString() {
        return String.format("[%s] %s: %s", this.date, this.username, this.content);
    }
}
