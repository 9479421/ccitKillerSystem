package vip.wqby.ccitserver.service;

import vip.wqby.ccitserver.pojo.CommonResult;

public interface QndxxService {
    public CommonResult testSession(String session);
    public CommonResult addTask_qndxx(String session,String qq, String phone, String cardID);
}
