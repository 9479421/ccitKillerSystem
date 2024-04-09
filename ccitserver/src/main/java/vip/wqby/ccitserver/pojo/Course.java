package vip.wqby.ccitserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private String username;
    private String password;
    private String courseName;
    private String teacherName;
    private String courseId;
    private String endTime;
    private String userId;
    private String token;
}
