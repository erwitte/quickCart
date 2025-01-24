package de.hsos.shared;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public class ReceiveToken {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private int not_before_policy;
    private String session_state;
    private String scope;

    public ReceiveToken() {}

    @JsonbCreator
    public ReceiveToken(
            @JsonbProperty("access_token") String access_token,
            @JsonbProperty("expires_in") String expires_in,
            @JsonbProperty("refresh_expires_in") String refresh_expires_in,
            @JsonbProperty("refresh_token") String refresh_token,
            @JsonbProperty("token_type") String token_type,
            @JsonbProperty("not-before-poliy") String not_before_policy,
            @JsonbProperty("session_state") String session_state,
            @JsonbProperty("scope") String scope
    ){
        this.access_token = access_token;
        this.expires_in = Integer.parseInt(expires_in);
        this.refresh_expires_in = Integer.parseInt(refresh_expires_in);
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.not_before_policy = Integer.parseInt(not_before_policy);
        this.session_state = session_state;
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(int refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getNot_before_policy() {
        return not_before_policy;
    }

    public void setNot_before_policy(int not_before_policy) {
        this.not_before_policy = not_before_policy;
    }

    public String getSession_state() {
        return session_state;
    }

    public void setSession_state(String session_state) {
        this.session_state = session_state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
