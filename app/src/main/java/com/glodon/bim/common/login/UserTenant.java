package com.glodon.bim.common.login;

import java.io.Serializable;

/**
 * 描述：
 * 作者：zhourf on 2017/9/13
 * 邮箱：zhourf@glodon.com
 */

public class UserTenant  implements Serializable {
    public int id;
    public boolean admin;//true  false
    public long tenantId;
    public String tenantName;
}
