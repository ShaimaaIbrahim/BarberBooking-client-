package com.google.barberbookingapp.Model.entities;

import com.google.firebase.firestore.FieldValue;

public class Notification {

    private String uid, title , content;
    private boolean read;
    private FieldValue serverTimestamp;

    public Notification() {
    }

    public FieldValue getServerTimestamp() {
        return serverTimestamp;
    }

    public Notification setServerTimestamp(FieldValue serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Notification setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Notification setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isRead() {
        return read;
    }

    public Notification setRead(boolean read) {
        this.read = read;
        return this;
    }

    public Notification(String uid, String title, String content, boolean read) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.read = read;
    }
}
