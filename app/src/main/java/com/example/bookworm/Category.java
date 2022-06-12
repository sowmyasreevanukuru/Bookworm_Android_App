package com.example.bookworm;

public class Category {

    private String category_name;
    private String imgid;

    public Category(String course_name, String imgid) {
        this.category_name = course_name;
        this.imgid = imgid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCourse_name(String course_name) {
        this.category_name = course_name;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }
}
