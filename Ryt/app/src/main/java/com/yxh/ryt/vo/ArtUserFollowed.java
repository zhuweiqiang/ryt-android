package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/18.
 *
 */
public class ArtUserFollowed implements Serializable{
    private String id;
    private User user;//关注着
    private User follower;//被关注者
    private String status;
    private String type;//1.关注艺术家 2.关注普通用户
    private long createDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }
}



