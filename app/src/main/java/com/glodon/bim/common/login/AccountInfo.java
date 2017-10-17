package com.glodon.bim.common.login;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：账户信息
 * 作者：zhourf on 2017/9/13
 * 邮箱：zhourf@glodon.com
 */

public class AccountInfo  implements Serializable {
    public String name;
    public String phone;
    public String email;
    public String qq;
    public String sex;// MALE   FEMALE
    public String gldAccountId;
    public List<UserTenant> userTenants;
}
