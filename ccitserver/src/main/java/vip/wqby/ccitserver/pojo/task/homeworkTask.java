package vip.wqby.ccitserver.pojo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class homeworkTask {
    private String username;
    private String password;
    private String businessID;
    private String time;
}
