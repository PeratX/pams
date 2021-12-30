package org.itxtech.pams.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "asset_log")
public class AssetLog {
    public static final int ACTION_IN = 0;
    public static final int ACTION_OUT = 1;
    public static final int ACTION_MODIFY = 2;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(nullable = false)
    private int amount = 0;

    @Column(nullable = false)
    private int action = ACTION_IN;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AssetLog{" +
                "id=" + id +
                ", asset=" + asset.getId() +
                ", amount=" + amount +
                ", action=" + action +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }
}
