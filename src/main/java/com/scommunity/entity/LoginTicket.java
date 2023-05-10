package com.scommunity.entity;

import java.util.Date;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/10 14:25
 */
//登陆凭证存储在数据库中
public class LoginTicket {
    //主键
    private int id;
    //用户id
    private int userId;
    //凭证,字符串
    private String ticket;
    //状态
    private int status;
    //到期的日期
    private Date expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
