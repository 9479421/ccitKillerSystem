package vip.wqby.ccitserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpDailyUser {
    private String _id;
    private String username;
    private String password;
    private String qq;
}
