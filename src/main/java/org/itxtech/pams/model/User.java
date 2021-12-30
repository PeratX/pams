package org.itxtech.pams.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public static final int ROLE_NONE = 0;
    public static final int ROLE_VIEWER = 1;
    public static final int ROLE_ADMIN = 2;

    @Id
    @Column
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Column
    private String token;

    @Column
    private int role = ROLE_NONE;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<AssetLog> logs;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
