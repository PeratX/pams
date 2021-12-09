package org.itxtech.pams.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    public static final int ROLE_NONE = 0;
    public static final int ROLE_VIEWER = 1;
    public static final int ROLE_ADMIN = 2;

    @Id
    @Column
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String token;

    @Column
    private int role = ROLE_NONE;

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
}
