package vip.wqby.ccitserver.service;

import vip.wqby.ccitserver.pojo.CommonResult;
import vip.wqby.ccitserver.pojo.CpDailyUser;

import java.util.List;

public interface CpDailyService {

    public CommonResult addTask(String username, String password,String qq,String cardID);
}
