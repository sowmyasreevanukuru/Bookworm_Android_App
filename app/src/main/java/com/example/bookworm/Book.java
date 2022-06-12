package com.example.bookworm;

public class Book {
    public String bookAuthor;
    public String bookCategory;
    public String bookDescription;
    public String bookName;
    public String bookPublisher;
    public String bookpdf;
    public String coverimg;
    int nod;
    int nol;
    int nov;
    public String userID;

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int bookid;
    public Book() {
    }

    public Book(String bookAuthor, String bookCategory, String bookDescription, String bookName, String bookPublisher, String bookpdf,String coverimg, int nod, int nol, int nov, String userID) {
        this.bookAuthor = bookAuthor;
        this.bookCategory = bookCategory;
        this.bookDescription = bookDescription;
        this.bookName = bookName;
        this.bookPublisher = bookPublisher;
        this.bookpdf = bookpdf;
        this.coverimg=coverimg;
        this.nod = nod;
        this.nol = nol;
        this.nov = nov;
        this.userID = userID;
    }

    public Book(String bookname, String bookauthor, String bookcategory,String coverimg,String bookpdf,String bookPublisher,String bookDescription) {
        this.bookName=bookname;
        this.bookAuthor=bookauthor;
        this.bookCategory=bookcategory;
        this.coverimg=coverimg;
        this.bookpdf=bookpdf;
        this.bookPublisher=bookPublisher;
        this.bookDescription=bookDescription;
    }

    public int getNod() {
        return nod;
    }

    public void setNod(int nod) {
        this.nod = nod;
    }

    public int getNol() {
        return nol;
    }

    public void setNol(int nol) {
        this.nol = nol;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }
    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookpdf() {
        return bookpdf;
    }

    public void setBookpdf(String bookpdf) {
        this.bookpdf = bookpdf;
    }
    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }
}
