package com.google.barberbookingapp.Model.FCM;

import java.util.List;

public class FcmResponce {

    private  int canonical_id;
    private List<Result> results;

    public FcmResponce() {
    }

    public int getCanonical_id() {
        return canonical_id;
    }

    public FcmResponce setCanonical_id(int canonical_id) {
        this.canonical_id = canonical_id;
        return this;
    }

    public List<Result> getResults() {
        return results;
    }

    public FcmResponce setResults(List<Result> results) {
        this.results = results;
        return this;
    }

    class Result{

        private String message_id;

        public Result() {
        }

        public String getMessage_id() {
            return message_id;
        }

        public Result setMessage_id(String message_id) {
            this.message_id = message_id;
            return this;
        }
    }
}
