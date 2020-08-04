package com.spappstudio.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String PAGES_TABLE_NAME = "BookFitnessPages";
    public static final String BOOKS_TABLE_NAME = "BookFitnessBooks";

    public static final String BOOKS_TABLE_COLUMN_ID = "id";
    public static final String BOOKS_TABLE_COLUMN_AUTHOR = "Author";
    public static final String BOOKS_TABLE_COLUMN_NAME = "Name";
    public static final String BOOKS_TABLE_COLUMN_PAGES_ALL = "Pages_All";
    public static final String BOOKS_TABLE_COLUMN_PAGE = "Page";
    public static final String BOOKS_TABLE_COLUMN_IS_END = "is_end";

    public DBHelper (Context context) {
        super(context, "BookFitnessDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookFitnessPages(id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Pages INTEGER);");
        db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Author TEXT, Name TEXT, Pages_All INTEGER, Page INTEGER, is_end INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Author TEXT, Name TEXT, Pages_All INTEGER, Page INTEGER, is_end INTEGER);");
                break;
            default:
                break;
        }
    }

    public boolean insert(String date, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("Pages", pages);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGES_ALL, book.pagesAll);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGE, book.page);
        contentValues.put(BOOKS_TABLE_COLUMN_IS_END, book.is_end);
        db.insert(BOOKS_TABLE_NAME, null, contentValues);
        return true;
    }

    public int getBooksCount () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT COUNT(*) FROM " + BOOKS_TABLE_NAME +";", null );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public boolean updatePages(String date, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("Pages", pages);
        db.update(PAGES_TABLE_NAME, contentValues, "Date = '" + date + "'", null);
        return true;
    }

    public boolean updatePages(int pages) {
        String date = getTodayDateString();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("Pages", pages);
        db.update(PAGES_TABLE_NAME, contentValues, "Date = '" + date + "'", null);
        return true;
    }

    public boolean updateAddPages(int pages) {
        pages += getPagesToday();
        String date = getTodayDateString();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("Pages", pages);
        db.update(PAGES_TABLE_NAME, contentValues, "Date = '" + date + "'", null);
        return true;
    }

    public boolean updatePageInBook(int book_id, int pages, int pages_all) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + BOOKS_TABLE_NAME + " SET " + BOOKS_TABLE_COLUMN_PAGE + " = " + pages + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = " + book_id + ";" ;
        db.execSQL(sql);
        if (pages == pages_all) {
            updateIsEndInBook(book_id, true);
        }
        return true;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGES_ALL, book.pagesAll);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGE, book.page);
        contentValues.put(BOOKS_TABLE_COLUMN_IS_END, book.is_end);
        db.update(BOOKS_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(book.id)});
        return true;
    }

    public boolean updateIsEndInBook(int book_id, boolean is_end) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql;
        if (is_end) {
            sql = "UPDATE " + BOOKS_TABLE_NAME + " SET " + BOOKS_TABLE_COLUMN_IS_END + " = " + 1 + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = " + book_id + ";" ;
        } else {
            sql = "UPDATE " + BOOKS_TABLE_NAME + " SET " + BOOKS_TABLE_COLUMN_IS_END + " = " + 0 + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = " + book_id + ";" ;
        }
        db.execSQL(sql);
        return true;
    }

    public int[] getAllPages() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("BookFitnessPages", null, null, null, null, null, null);
        int c = cursor.getCount();
        int res[] = new int[c];
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            res[i] = cursor.getInt(cursor.getColumnIndex("Pages"));
            i++;
            cursor.moveToNext();
        }
        return res;
    }

    public ArrayList getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> books = new ArrayList<Book>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            books.add(new Book(
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE)),
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_IS_END))
            ));
            cursor.moveToNext();
        }
        return books;
    }

    public Book getBookByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BOOKS_TABLE_NAME, null, "id = '" + String.valueOf(id) + "'", null, null, null, null);
        cursor.moveToFirst();
        return new Book(
                id,
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_IS_END))
        );
    }

    public Book getLastInsertedBook() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
        return new Book(
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_IS_END))
        );
    }

    public int getPages(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("BookFitnessPages", null, "Date = '" + date + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("Pages"));
        } else {
            this.insert(date, 0);
            return 0;
        }
    }

    public int[] getPagesPerWeek() {
        int week[] = new int[7];
        week[0] = getPagesToday();
        for (int i = 1; i < 7; i++) {
            week[i] = getPages(getLastDateString(i));
        }
        return week;
    }

    public int[] getPagesPerMonth(int m) {
        int month[] = new int[m];
        month[0] = getPagesToday();
        for (int i = 1; i < m; i++) {
            month[i] = getPages(getLastDateString(i));
        }
        return month;
    }

    public int[] getPagesPerMonth() {
        int m = getTodayDay();
        int month[] = new int[m];
        month[0] = getPagesToday();
        for (int i = 1; i < m; i++) {
            month[i] = getPages(getLastDateString(i));
        }
        return month;
    }

    public int getPagesForWeek() {
        int count = 0;
        int week[] = getPagesPerWeek();
        for (int i = 0; i < 7; i++) {
            count += week[i];
        }
        return count;
    }

    public int getPagesForMount() {
        int m = getTodayDay();
        int count = 0;
        int month[] = getPagesPerMonth(m);
        for (int i = 0; i < month.length; i++) {
            count += month[i];
        }
        return count;
    }

    public int getPagesToday() {
        return  getPages(getTodayDateString());
    }

    public int getPagesYesterday() {
        return getPages(getYesterdayDateString());
    }

    public String getDayOfWeek(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        Date dt1 = null;
        try {
            dt1 = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2 = new SimpleDateFormat("EE");
        return format2.format(dt1);
    }

    public String getTodayDateString() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        return formatForDateNow.format(dateNow);
    }

    public int getTodayDay() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd");
        return Integer.valueOf(formatForDateNow.format(dateNow));
    }

    public String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(yesterday());
    }

    public String getLastDateString(int days) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(yesterday(days));
    }

    public Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public Date yesterday(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }
    public int getTodayDayOfWeek() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public void deleteBook(int book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BOOKS_TABLE_NAME, "id = " + book_id, null);
    }

    public int getHighScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(Pages) FROM " + PAGES_TABLE_NAME + ";", null);
        cursor.moveToFirst();
        int highScore = cursor.getInt(0);
        cursor.close();
        return highScore;
    }

}
