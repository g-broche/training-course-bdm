package com.example.bdm.dto;

public class RequestUserGdprUpdate {
    private boolean hasAcceptedGdpr;

    public RequestUserGdprUpdate() {;
    }

    public RequestUserGdprUpdate(boolean hasAcceptedGdpr) {
        this.hasAcceptedGdpr = hasAcceptedGdpr;
    }

    public boolean getHasAcceptedGdpr() {
        return hasAcceptedGdpr;
    }

    public void setHasAcceptedGdpr(boolean hasAcceptedGdpr) {
        this.hasAcceptedGdpr = hasAcceptedGdpr;
    }
}
