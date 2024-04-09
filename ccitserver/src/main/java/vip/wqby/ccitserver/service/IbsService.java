package vip.wqby.ccitserver.service;

import vip.wqby.ccitserver.pojo.CommonResult;

import javax.websocket.server.PathParam;

public interface IbsService {
    public CommonResult getHomeWorkList(String username, String password);

    public CommonResult submitTask(String username, String password, String businessID, String cardID);

    public CommonResult getCardId(String authorization);

    public CommonResult getCourseList(String username, String password);

    public CommonResult submitTask_rollCall(String username, String password, String courseId, String userId, String token, String cardID);
}
