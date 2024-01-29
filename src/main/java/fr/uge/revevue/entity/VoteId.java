package fr.uge.revevue.entity;

import java.io.Serializable;
import java.util.Objects;

public class VoteId implements Serializable {

    private Long user;
    private Long code;

    public VoteId() {}

    public VoteId(Long user, Long code) {
        this.user = user;
        this.code = code;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}