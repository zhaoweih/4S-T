package com.zhaoweihao.architechturesample.bean.vote;

import java.io.Serializable;
import java.util.List;

public class Add implements Serializable {

    private int id;
    private Object tecId;
    private String teacherId;
    private String title;
    private int choiceNum;
    private Object startDate;
    private Object endDate;
    private Object imgUrl;
    private int choiceMode;
    private int choiceMax;
    private List<Select> selectList;
    private int courseId;

    private Integer chapter_id;

    public Integer getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(Integer chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getTecId() {
        return tecId;
    }

    public void setTecId(Object tecId) {
        this.tecId = tecId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }

    public Object getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Object imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getChoiceMode() {
        return choiceMode;
    }

    public void setChoiceMode(int choiceMode) {
        this.choiceMode = choiceMode;
    }

    public int getChoiceMax() {
        return choiceMax;
    }

    public void setChoiceMax(int choiceMax) {
        this.choiceMax = choiceMax;
    }

    public List<Select> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<Select> selectList) {
        this.selectList = selectList;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
