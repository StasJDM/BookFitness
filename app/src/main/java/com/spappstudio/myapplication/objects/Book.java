package com.spappstudio.myapplication.objects;

public class Book {

    public int id;
    public String author;
    public String name;
    public String type;
    public int pagesAll;
    public int page;
    public String start_date;
    public String end_date;
    public String end_year;
    public int rating;

    public Book(int id, String author, String name, String type) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.type = type;
    }

    public Book(int id, String author, String name, String type, int pagesAll, int page) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.type = type;
        this.pagesAll = pagesAll;
        this.page = page;
    }

    public Book(int id, String author, String name, String type, String end_year) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.type = type;
        this.end_year = end_year;
    }

    public Book(int id, String author, String name, String type, int pagesAll, int page, String start_date) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.type = type;
        this.pagesAll = pagesAll;
        this.page = page;
        this.start_date = start_date;
    }

    public Book(int id, String author, String name, String type, String start_date, String end_date) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getTitle () {
        return author + " - " + name;
    }

    public int getPercent() {
        double a = page * 1.0;
        double b = pagesAll * 1.0;
        return (int)Math.ceil(a / b * 100);
    }

    public int addPages(int pages) {
        if (pages + page < pagesAll) {
            page += pages;
        } else {
            page = pagesAll;
        }
        return page;
    }
}
