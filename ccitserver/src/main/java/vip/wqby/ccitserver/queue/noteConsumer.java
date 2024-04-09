package vip.wqby.ccitserver.queue;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import vip.wqby.ccitserver.pojo.task.noteTask;

public class noteConsumer extends Thread {
    @Override
    public void run() {
        try {
            for (; ; ) {
                noteTask poll = null;
                try {
                    poll = AllQueue.noteTaskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (poll != null) {
                    Credential cred = new Credential("", "");
                    HttpProfile httpProfile = new HttpProfile();
                    httpProfile.setReqMethod("POST");
                    httpProfile.setConnTimeout(60);
                    httpProfile.setEndpoint("sms.tencentcloudapi.com");
                    ClientProfile clientProfile = new ClientProfile();
                    clientProfile.setSignMethod("HmacSHA256");
                    clientProfile.setHttpProfile(httpProfile);
                    SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
                    SendSmsRequest req = new SendSmsRequest();
                    String sdkAppId = "1400665385";
                    req.setSmsSdkAppId(sdkAppId);
                    String signName = "王国权个人学习网";
                    req.setSignName(signName);
                    String templateId = poll.getTemplateId();
                    req.setTemplateId(templateId);
                    String[] templateParamSet = poll.getArgs();
                    req.setTemplateParamSet(templateParamSet);
                    String[] phoneNumberSet = {"+86"+poll.getPhone()};
                    req.setPhoneNumberSet(phoneNumberSet);
                    SendSmsResponse res = client.SendSms(req);
                    System.out.println(SendSmsResponse.toJsonString(res));
                }
            }
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
