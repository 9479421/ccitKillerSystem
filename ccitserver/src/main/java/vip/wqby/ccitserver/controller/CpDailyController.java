package vip.wqby.ccitserver.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.pojo.task.cpDailyTask;
import vip.wqby.ccitserver.queue.AllQueue;
import vip.wqby.ccitserver.service.CpDailyService;

import javax.annotation.Resource;

@RestController
@CrossOrigin("*")
public class CpDailyController {
    @Resource
    CpDailyService cpDailyService;


    @PostMapping("/addTask")
    public CommonResult addTask(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("qq") String qq,@RequestParam("cardID") String cardID){
        return cpDailyService.addTask(username,password,qq,cardID);
    }
}
