package com.example.bdm.dto;

import com.example.bdm.model.AppUser;

public class ResponseUserGdpr {
    private boolean hasAcceptedGdpr;

    public ResponseUserGdpr() {
    }

    public ResponseUserGdpr(AppUser user) {
        this.hasAcceptedGdpr = user.getGdpr();
    }

    public boolean isHasAcceptedGdpr() {
        return hasAcceptedGdpr;
    }

    public void setHasAcceptedGdpr(boolean hasAcceptedGdpr) {
        this.hasAcceptedGdpr = hasAcceptedGdpr;
    }
}
