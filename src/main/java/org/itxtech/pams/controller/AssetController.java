package org.itxtech.pams.controller;

import org.itxtech.pams.model.Asset;
import org.itxtech.pams.model.AssetLog;
import org.itxtech.pams.model.User;
import org.itxtech.pams.repo.AssetLogRepo;
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

    @Autowired
    private AssetLogRepo logRepo;

    static class CheckResult {
        public Response response;
        public User user;
    }

    private CheckResult checkToken(String token, int role) {
        var user = userRepo.findByToken(token);
        var result = new CheckResult();
        result.response = new Response();
        var resp = result.response;
        if (user.isEmpty()) {
            resp.code = Response.CODE_ERR;
            resp.msg = "非法请求";
        } else {
            if (user.get().getRole() >= role) {
                resp.code = Response.CODE_OK;
                resp.msg = "成功";
                result.user = user.get();
            } else {
                resp.code = Response.CODE_ERR;
                resp.msg = "无权访问，请联系管理升级权限";
            }
        }
        return result;
    }

    @GetMapping("/list")
    public Response listAssets(@RequestHeader String token) {
        var resp = checkToken(token, User.ROLE_VIEWER);
        if (resp.response.code == Response.CODE_OK) {
            resp.response.data = assetRepo.findAllByDisabled(false);
        }
        return resp.response;
    }

    @PostMapping("/edit/{id}")
    public Response editAsset(
            @RequestHeader String token, @PathVariable String id,
            @RequestBody Map<String, String> body
    ) { // does not allow to edit amount directly, use in out
        var r = checkToken(token, User.ROLE_ADMIN);
        if (r.response.code == Response.CODE_OK) {
            var asset = assetRepo.findById(Long.parseLong(id));
            if (asset.isEmpty()) {
                r.response.code = Response.CODE_ERR;
                r.response.msg = "未找到资产";
            } else {
                var a = asset.get();
                var log = new AssetLog();
                log.setUser(r.user);
                log.setAsset(a);
                log.setAction(AssetLog.ACTION_MODIFY);
                var msg = "";

                var label = body.get("label");
                var desc = body.get("desc");
                var value = body.get("value");
//                var amount = body.get("amount");
                if (label != null) {
                    msg += " Label: " + a.getLabel() + " -> " + label;
                    a.setLabel(label);
                }
                if (desc != null) {
                    msg += " Desc: " + a.getDescription() + " -> " + desc;
                    a.setDescription(desc);
                }
                if (value != null) {
                    msg += " Value: " + a.getValue() + " -> " + value;
                    a.setValue(Integer.parseInt(value));
                }
//                if (amount != null) {
//                    msg += " Amount: " + a.getAmount() + " -> " + amount;
//                    a.setAmount(Integer.parseInt(amount));
//                }
                log.setMessage(msg);

                logRepo.save(log);
                assetRepo.save(a);
            }
        }
        return r.response;
    }

    @PostMapping("/warehouse/{id}")
    public Response warehouse(
            @RequestHeader String token, @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        var r = checkToken(token, User.ROLE_ADMIN);
        if (r.response.code == Response.CODE_OK) {
            var asset = assetRepo.findById(Long.parseLong(id));
            if (asset.isEmpty()) {
                r.response.code = Response.CODE_ERR;
                r.response.msg = "未找到资产";
            } else {
                var a = asset.get();
                var log = new AssetLog();
                log.setUser(r.user);
                log.setAsset(a);
                log.setAction(AssetLog.ACTION_MODIFY);
                var msg = "";
                var amount = Integer.parseInt(body.getOrDefault("amount", "0"));
                var message = body.getOrDefault("message", "");
                var action = Integer.parseInt(body.getOrDefault("action", "0"));

                if (action == AssetLog.ACTION_IN) {
                    msg = "入库数量： " + amount + " 原数量：" + a.getAmount() + " 备注：" + message;
                    a.setAmount(a.getAmount() + amount);
                } else if (action == AssetLog.ACTION_OUT) {
                    msg = "出库数量： " + amount + " 原数量：" + a.getAmount() + " 备注：" + message;
                    if (amount > a.getAmount()) {
                        r.response.code = Response.CODE_ERR;
                        r.response.msg = "出库数量大于总数量";
                        return r.response;
                    }
                    a.setAmount(a.getAmount() - amount);
                }
                log.setMessage(msg);
                logRepo.save(log);

                assetRepo.save(a);
            }
        }
        return r.response;
    }

    @GetMapping("/delete/{id}")
    public Response deleteAsset(@RequestHeader String token, @PathVariable String id) {
        var resp = checkToken(token, User.ROLE_ADMIN);
        if (resp.response.code == Response.CODE_OK) {
            var asset = assetRepo.findById(Long.parseLong(id));
            if (asset.isEmpty()) {
                resp.response.code = Response.CODE_ERR;
                resp.response.msg = "未找到货品";
            } else {
                var a = asset.get();
                var log = new AssetLog();
                log.setUser(resp.user);
                log.setAction(AssetLog.ACTION_MODIFY);
                log.setAsset(a);
                log.setMessage("标记删除货品：" + a);
                logRepo.save(log);
                a.setDisabled(true);
                assetRepo.save(a);
            }
        }
        return resp.response;
    }

    @PostMapping("/add")
    public Response addAsset(@RequestHeader String token,
                             @RequestBody Map<String, String> body) {
        var resp = checkToken(token, User.ROLE_ADMIN);
        if (resp.response.code == Response.CODE_OK) {
            var a = new Asset();
            var label = body.getOrDefault("label", "");
            var desc = body.getOrDefault("desc", "");
            var value = body.getOrDefault("value", "0");
            var amount = body.getOrDefault("amount", "0");
            a.setLabel(label);
            a.setDescription(desc);
            a.setValue(Integer.parseInt(value));
            a.setAmount(Integer.parseInt(amount));
            a = assetRepo.save(a);

            var log = new AssetLog();
            log.setAmount(a.getAmount());
            log.setUser(resp.user);
            log.setAsset(a);
            log.setMessage("新增 " + a);

            logRepo.save(log);
        }
        return resp.response;
    }

    @PostMapping("/logs")
    public Response getLogs(@RequestHeader String token, @RequestBody Map<String, String> body) {
        var result = checkToken(token, User.ROLE_VIEWER);
        if (result.response.code == Response.CODE_OK) {
            var id = body.getOrDefault("id", "");
            if ("".equals(id)) {
                result.response.data = logRepo.findAll();
            } else {
                result.response.data = logRepo.findByAsset_Id(Long.parseLong(id));
            }
        }
        return result.response;
    }
}
