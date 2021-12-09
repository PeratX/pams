package org.itxtech.pams.controller;

import org.itxtech.pams.model.Asset;
import org.itxtech.pams.model.User;
import org.itxtech.pams.repo.AssetRepo;
import org.itxtech.pams.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AssetRepo assetRepo;

    private Response checkToken(String token, int role) {
        var user = userRepo.findByToken(token);
        var resp = new Response();
        if (user.isEmpty()) {
            resp.code = Response.CODE_ERR;
            resp.msg = "非法请求";
        } else {
            if (user.get().getRole() >= role) {
                resp.code = Response.CODE_OK;
                resp.msg = "成功";
            } else {
                resp.code = Response.CODE_ERR;
                resp.msg = "无权访问，请联系管理升级权限";
            }
        }
        return resp;
    }

    @GetMapping("/list")
    public Response listAssets(@RequestHeader String token) {
        var resp = checkToken(token, User.ROLE_VIEWER);
        if (resp.code == Response.CODE_OK) {
            resp.data = assetRepo.findAll();
        }
        return resp;
    }

    @PostMapping("/edit/{id}")
    public Response editAsset(
            @RequestHeader String token, @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        var resp = checkToken(token, User.ROLE_ADMIN);
        if (resp.code == Response.CODE_OK) {
            var asset = assetRepo.findById(Long.parseLong(id));
            if (asset.isEmpty()) {
                resp.code = Response.CODE_ERR;
                resp.msg = "未找到资产";
            } else {
                var a = asset.get();
                var label = body.get("label");
                var desc = body.get("desc");
                var value = body.get("value");
                if (label != null) {
                    a.setLabel(label);
                }
                if (desc != null) {
                    a.setDescription(desc);
                }
                if (value != null) {
                    a.setValue(Integer.parseInt(value));
                }
                resp.data = a;
                assetRepo.save(a);
            }
        }
        return resp;
    }

    @GetMapping("/delete/{id}")
    public Response deleteAsset(@RequestHeader String token, @PathVariable String id) {
        var resp = checkToken(token, User.ROLE_ADMIN);
        if (resp.code == Response.CODE_OK) {
            var asset = assetRepo.findById(Long.parseLong(id));
            if (asset.isEmpty()) {
                resp.code = Response.CODE_ERR;
                resp.msg = "未找到资产";
            } else {
                var a = asset.get();
                assetRepo.delete(a);
            }
        }
        return resp;
    }

    @PostMapping("/add")
    public Response addAsset(@RequestHeader String token,
                             @RequestBody Map<String, String> body) {
        var resp = checkToken(token, User.ROLE_ADMIN);
        if (resp.code == Response.CODE_OK) {
            var a = new Asset();
            var label = body.getOrDefault("label", "");
            var desc = body.getOrDefault("desc", "");
            var value = body.getOrDefault("value", "0");
            a.setLabel(label);
            a.setDescription(desc);
            a.setValue(Integer.parseInt(value));
            resp.data = a;
            assetRepo.save(a);
        }
        return resp;
    }
}
