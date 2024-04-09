package vip.wqby.ccitserver.queue;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import vip.wqby.ccitserver.config.SpringUtil;
import vip.wqby.ccitserver.pojo.task.emailTask;


public class emailConsumer extends Thread{
    @Override
    public void run() {
        for (;;){
            emailTask poll = null;
            try {
                poll = AllQueue.emailTaskQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (poll != null) {
                try {
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom("wangguoquan@wqby.vip");
                    simpleMailMessage.setTo(poll.getTo());
                    simpleMailMessage.setText(poll.getBody());
                    simpleMailMessage.setSubject(poll.getSubject());
                    JavaMailSenderImpl javaMailSender = SpringUtil.getBean(JavaMailSenderImpl.class);
                    javaMailSender.send(simpleMailMessage);
                    //企业邮箱 最好也延迟一下 容易频繁
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}