package com.spappstudio.myapplication.objects;

public class Book {

    public int id;
    public String author;
    public String name;
    public int pagesAll;
    public int page;
    public boolean is_end;

    public Book(int id, String author, String name, int pagesAll, int page, int is_end) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.pagesAll = pagesAll;
        this.page = page;
        if (is_end == 1) {
            this.is_end = true;
        } else {
            this.is_end = false;
        }
    }

    public Book(int id, String author, String name) {
        this.id = id;
        this.author = author;
        this.name = name;
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
