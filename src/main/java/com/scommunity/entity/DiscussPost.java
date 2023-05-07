package com.scommunity.entity;

import java.util.Date;

/**
 * @author sss
 * @version 1.0
 * @date 2023/5/6 10:36
 */
public class DiscussPost {
    //帖子id
    private int id;
    //谁发布的，关联user
    private int userId;
    //帖子标题
    private String title;
    //帖子内容
    private String content;
    //帖子类型，0-普通帖子，1-置顶帖子
    private int type;
    //帖子状态，0-正常，1-精华，2-拉黑
    private int status;
    //帖子发布时间
    private Date createTime;
    //帖子评论的数量
    private int commentCount;
    //分数（用于排名）
    private double score;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
