package org.itxtech.pams.controller;

import org.itxtech.pams.repo.UserRepo;
import org.itxtech.pams.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepo repo;

    @PostMapping("/login")
    public Response login(@RequestBody Map<String, String> body) throws NoSuchAlgorithmException {
        // password is md5 hashed already
        var user = repo.findById(body.getOrDefault("username", ""));
        var resp = new Response();
        if (user.isEmpty()) {
            resp.code = Response.CODE_ERR;
            resp.msg = "错误的用户名或密码";
        } else {
            var u = user.get();
            if (body.getOrDefault("password", "").toLowerCase().equals(u.getPassword())) {
                resp.code = Response.CODE_OK;
                resp.msg = "登录成功";
                // this will invalidate all login sessions
                u.setToken(Util.checksum(u.getName() + u.getPassword() + System.currentTimeMillis()));
                resp.data = u.getToken();
                repo.save(u);
            } else {
                resp.code = Response.CODE_ERR;
                resp.msg = "错误的用户名或密码";
            }
        }
        return resp;
    }

    @PostMapping("/user")
    public Response name(@RequestBody Map<String, String> map) {
        var user = repo.findByToken(map.getOrDefault("token", ""));
        var resp = new Response();
        if (user.isEmpty()) {
            resp.code = Response.CODE_ERR;
            resp.msg = "错误的Token";
        } else {
            resp.code = Response.CODE_OK;
            resp.msg = "成功";
            var userInfo = new UserInfo();
            userInfo.name = user.get().getName();
            userInfo.role = user.get().getRole();
            resp.data = userInfo;
        }
        return resp;
    }

    static class UserInfo {
        public String name;
        public int role;
    }
}
