package vip.wqby.ccitserver.pojo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class rollCallTask {
    private String username;
    private String password;
    private String courseId;
    private String userId;
    private String token;
}
