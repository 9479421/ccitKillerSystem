package vip.wqby.ccitserver.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.service.IbsService;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
@CrossOrigin(origins = "*")
public class IbsController {
    @Resource
    IbsService ibsService;

    @GetMapping("/getHomeWorkList")
    public CommonResult getHomeWorkList(@PathParam("username") String username, @PathParam("password") String password) {
        return ibsService.getHomeWorkList(username, password);
    }

    @PostMapping("/submitTask")
    public CommonResult submitTask(@PathParam("username")String username,
                                   @PathParam("password")String password,
                                   @PathParam("businessID")String businessID,
                                   @PathParam("cardID")String cardID){
        return ibsService.submitTask(username,password,businessID,cardID);
    }

    @GetMapping("/getCardId")
    public CommonResult getCardId(@PathParam("authorization") String authorization) {
        return ibsService.getCardId(authorization);
    }

    @GetMapping("/getCourseList")
    public CommonResult getCourseList(@PathParam("username")String username,@PathParam("password")String password){
        return ibsService.getCourseList(username,password);
    }

    @PostMapping("/submitTask_rollCall")
    public CommonResult submitTask_qd(@RequestBody String request){
        JSONObject json = JSONObject.parseObject(request);
        System.out.println(json);
        String username = json.getString("username");
        String password = json.getString("password");
        String courseId = json.getString("courseId");
        String userId = json.getString("userId");
        String token = json.getString("token");
        String cardID = json.getString("cardID");
        return ibsService.submitTask_rollCall(username,password,courseId,userId,token,cardID);
    }


}
