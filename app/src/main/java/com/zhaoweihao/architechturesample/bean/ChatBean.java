package com.zhaoweihao.architechturesample.bean;

public class ChatBean {


    /**
     * id : 1
     * sender_id : 10
     * receiver_id : 13
     * msg_content : 测试消息
     * send_type : 0
     * msg_time : 1547027069110
     * sender_avatar : null
     * receiver_avatar : null
     * course_id : 0
     * class_id : 0
     */

    private int id;
    private int sender_id;
    private int receiver_id;
    private String msg_content;
    private int send_type;
    private long msg_time;
    private Object sender_avatar;
    private Object receiver_avatar;
    private int course_id;
    private int class_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public int getSend_type() {
        return send_type;
    }

    public void setSend_type(int send_type) {
        this.send_type = send_type;
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }

    public Object getSender_avatar() {
        return sender_avatar;
    }

    public void setSender_avatar(Object sender_avatar) {
        this.sender_avatar = sender_avatar;
    }

    public Object getReceiver_avatar() {
        return receiver_avatar;
    }

    public void setReceiver_avatar(Object receiver_avatar) {
        this.receiver_avatar = receiver_avatar;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "id=" + id +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", msg_content='" + msg_content + '\'' +
                ", send_type=" + send_type +
                ", msg_time=" + msg_time +
                ", sender_avatar=" + sender_avatar +
                ", receiver_avatar=" + receiver_avatar +
                ", course_id=" + course_id +
                ", class_id=" + class_id +
                '}';
    }
}
