package vip.wqby.ccitserver.controller;

import org.springframework.web.bind.annotation.*;
import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.service.QndxxService;
import vip.wqby.ccitserver.service.impl.QndxxServiceImpl;

import javax.annotation.Resource;

@RestController
@CrossOrigin("*")
public class QndxxController {
    @Resource
    QndxxService qndxxService = new QndxxServiceImpl();

    @GetMapping("/testSession")
    public CommonResult testSession(@RequestParam("session")String session){
        return qndxxService.testSession(session);
    }

    @PostMapping("addTask_qndxx")
    public CommonResult addTask_qndxx(@RequestParam("session")String session, @RequestParam("qq")String qq,@RequestParam("phone")String phone,@RequestParam("cardID")String cardID){
        return qndxxService.addTask_qndxx(session,qq,phone,cardID);
    }
}
