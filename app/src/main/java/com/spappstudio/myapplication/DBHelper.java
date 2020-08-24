package com.spappstudio.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.spappstudio.myapplication.objects.Book;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String PAGES_TABLE_NAME = "BookFitnessPages";
    private static final String BOOKS_TABLE_NAME = "BookFitnessBooks";
    private static final String WISHFUL_BOOKS_TABLE_NAME = "BookFitnessWishfulBooks";
    private static final String WISHFUL_BOOKS_TABLE_NAME_OLD = "BookFitnessWishfulBooksOld";

    private static final String PAGES_TABLE_COLUMN_ID = "id";
    private static final String PAGES_TABLE_COLUMN_DATE = "Date";
    private static final String PAGES_TABLE_COLUMN_PAGES = "Pages";
    private static final String PAGES_TABLE_COLUMN_BOOK_ID = "Book_id";

    private static final String BOOKS_TABLE_COLUMN_ID = "id";
    private static final String BOOKS_TABLE_COLUMN_AUTHOR = "Author";
    private static final String BOOKS_TABLE_COLUMN_NAME = "Name";
    private static final String BOOKS_TABLE_COLUMN_TYPE = "Type";
    private static final String BOOKS_TABLE_COLUMN_PAGES_ALL = "Pages_All";
    private static final String BOOKS_TABLE_COLUMN_PAGE = "Page";
    private static final String BOOKS_TABLE_COLUMN_START_DATE = "Start_Date";
    private static final String BOOKS_TABLE_COLUMN_END_DATE = "End_Date";
    private static final String BOOKS_TABLE_COLUMN_END_YEAR = "End_Year";
    private static final String BOOKS_TABLE_COLUMN_RATING = "Rating";
    private static final String BOOKS_TABLE_COLUMN_IS_END = "is_end"; // Outdated

    private static final String BOOK_TYPE_CURRENT = "current";
    private static final String BOOK_TYPE_ARCHIVE = "archive";
    private static final String BOOK_TYPE_WISHFUL = "wishful";

    public DBHelper (Context context) {
        super(context, "BookFitnessDB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BookFitnessPages(id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, Pages INTEGER, Book_id INTEGER);");
        db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Author TEXT, Name TEXT, Type TEXT, Pages_All INTEGER, Page INTEGER, Start_Date TEXT, End_Date TEXT, End_Year TEXT, Rating INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Author TEXT, Name TEXT, Pages_All INTEGER, Page INTEGER, is_end INTEGER);");
                db.execSQL("ALTER TABLE " + PAGES_TABLE_NAME + " ADD COLUMN " + PAGES_TABLE_COLUMN_BOOK_ID + " INTEGER;");
                break;
            case 2:
                Log.d("LOG", "IT'S OKEY");
                db.execSQL("ALTER TABLE " + PAGES_TABLE_NAME + " ADD COLUMN " + PAGES_TABLE_COLUMN_BOOK_ID + " INTEGER;");
                db.execSQL("ALTER TABLE " + BOOKS_TABLE_NAME + " RENAME TO " + WISHFUL_BOOKS_TABLE_NAME_OLD + ";");
                db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Author TEXT, Name TEXT, Type TEXT, Pages_All INTEGER, Page INTEGER, Start_Date TEXT, End_Date TEXT, End_Year TEXT, Rating INTEGER);");
                db.execSQL("INSERT INTO " + BOOKS_TABLE_NAME + "(id, Author, Name, Pages_All, Page, Type) SELECT id, Author, Name, Pages_All, Page, is_end FROM " + WISHFUL_BOOKS_TABLE_NAME_OLD + ";");
                db.execSQL("DROP TABLE IF EXISTS " + WISHFUL_BOOKS_TABLE_NAME_OLD + ";");
                db.execSQL("UPDATE " + BOOKS_TABLE_NAME + " SET Type = '" + BOOK_TYPE_CURRENT + "' WHERE Type = '0';");
                db.execSQL("UPDATE " + BOOKS_TABLE_NAME + " SET Type = '" + BOOK_TYPE_ARCHIVE + "' WHERE Type = '1';");
                break;
            default:
                break;
        }
    }

    public void insertPages(String date, int pages, int book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAGES_TABLE_COLUMN_DATE, date);
        contentValues.put(PAGES_TABLE_COLUMN_PAGES, pages);
        contentValues.put(PAGES_TABLE_COLUMN_BOOK_ID, book_id);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
    }

    public void insertPages(String date, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAGES_TABLE_COLUMN_DATE, date);
        contentValues.put(PAGES_TABLE_COLUMN_PAGES, pages);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
    }

    public void insertPages(int pages, int book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAGES_TABLE_COLUMN_DATE, getTodayDateString());
        contentValues.put(PAGES_TABLE_COLUMN_PAGES, pages);
        contentValues.put(PAGES_TABLE_COLUMN_BOOK_ID, book_id);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
    }

    public void insertPages(int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAGES_TABLE_COLUMN_DATE, getTodayDateString());
        contentValues.put(PAGES_TABLE_COLUMN_PAGES, pages);
        db.insert(PAGES_TABLE_NAME, null, contentValues);
    }

    public int getPages(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + PAGES_TABLE_COLUMN_PAGES + ") FROM " + PAGES_TABLE_NAME + " WHERE Date='" + date + "';", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public int[] getPagesPerWeek() {
        int[] week = new int[7];
        week[0] = getPagesToday();
        for (int i = 1; i < 7; i++) {
            week[i] = getPages(getLastDateString(i));
        }
        return week;
    }

    public int[] getPagesPerMonth(int m) {
        int[] month = new int[m];
        month[0] = getPagesToday();
        for (int i = 1; i < m; i++) {
            month[i] = getPages(getLastDateString(i));
        }
        return month;
    }

    public int[] getPagesPerMonth() {
        int m = getTodayDay();
        int[] month = new int[m];
        month[0] = getPagesToday();
        for (int i = 1; i < m; i++) {
            month[i] = getPages(getLastDateString(i));
        }
        return month;
    }

    public int getPagesForWeek() {
        int count = 0;
        int[] week = getPagesPerWeek();
        for (int i = 0; i < 7; i++) {
            count += week[i];
        }
        return count;
    }

    public int getPagesForMount() {
        int m = getTodayDay();
        int count = 0;
        int[] month = getPagesPerMonth(m);
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

    public void insertBook(Book book, String type) {
        switch (type) {
            case BOOK_TYPE_ARCHIVE:
                insertArchiveBook(book);
                break;
            case BOOK_TYPE_CURRENT:
                insertCurrentBook(book);
                break;
            case BOOK_TYPE_WISHFUL:
                insertWishfulBook(book);
                break;
            default:
                break;
        }
    }

    public void updateBook(Book book, String type) {
        switch (type) {
            case BOOK_TYPE_ARCHIVE:
                updateArchiveBook(book);
                break;
            case BOOK_TYPE_CURRENT:
                updateCurrentBook(book);
                break;
            case BOOK_TYPE_WISHFUL:
                updateWishfulBook(book);
                break;
            default:
                break;
        }
    }

    public void insertCurrentBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_TYPE, book.type);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGES_ALL, book.pagesAll);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGE, book.page);
        contentValues.put(BOOKS_TABLE_COLUMN_START_DATE, book.start_date);
        db.insert(BOOKS_TABLE_NAME, null, contentValues);
    }

    public void insertArchiveBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_TYPE, book.type);
        if (!book.end_year.isEmpty()) {
            contentValues.put(BOOKS_TABLE_COLUMN_END_YEAR, book.end_year);
        }
        db.insert(BOOKS_TABLE_NAME, null, contentValues);
    }

    public void insertWishfulBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_TYPE, book.type);
        db.insert(BOOKS_TABLE_NAME, null, contentValues);
    }

    public Book getWishfulBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = '" + id +"' AND " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_WISHFUL + "';", null);
        cursor.moveToFirst();
        Book book = new Book(
                id,
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE))
        );
        cursor.close();
        return book;
    }

    public Book getCurrentBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = '" + id +"' AND " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_CURRENT + "';", null);
        cursor.moveToFirst();
        Book book = new Book(
                id,
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE))
        );
        cursor.close();
        return book;
    }

    public Book getArchiveBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = '" + id +"' AND " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_ARCHIVE + "';", null);
        cursor.moveToFirst();
        Book book = new Book(
                id,
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE))
        );
        cursor.close();
        return book;
    }

    public int getWishfulBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_WISHFUL + "';", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return  count;
    }

    public int getArchiveBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_ARCHIVE + "';", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return  count;
    }

    public int getCurrentBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_CURRENT + "';", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return  count;
    }

    public ArrayList<Book> getAllWishfulBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> wishfulBooks = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_WISHFUL + "';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            wishfulBooks.add(new Book(
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return wishfulBooks;
    }

    public ArrayList<Book> getAllArchiveBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> books = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_ARCHIVE + "';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            books.add(new Book(
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_END_DATE)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_END_DATE))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return books;
    }

    public ArrayList<Book> getAllCurrentBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Book> books = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOKS_TABLE_COLUMN_TYPE + " = '" + BOOK_TYPE_CURRENT  + "';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            books.add(new Book(
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                    cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE)),
                    cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_START_DATE))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return books;
    }

    public int getPagesInBook(int book_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + BOOKS_TABLE_COLUMN_PAGE + " FROM " + BOOKS_TABLE_NAME + " WHERE id ='" + book_id + "';", null);
        cursor.moveToNext();
        int pages = cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE));
        cursor.close();
        return pages;
    }

    public boolean updateCurrentBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGES_ALL, book.pagesAll);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGE, book.page);
        db.update(BOOKS_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(book.id)});
        return true;
    }

    public boolean updateArchiveBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGES_ALL, book.pagesAll);
        contentValues.put(BOOKS_TABLE_COLUMN_PAGE, book.page);
        contentValues.put(BOOKS_TABLE_COLUMN_END_YEAR, book.end_year);
        db.update(BOOKS_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(book.id)});
        return true;
    }


    public void updateWishfulBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_AUTHOR, book.author);
        contentValues.put(BOOKS_TABLE_COLUMN_NAME, book.name);
        db.update(WISHFUL_BOOKS_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(book.id)});
    }

    public int getBooksCount () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT COUNT(*) FROM " + BOOKS_TABLE_NAME +";", null );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public boolean updatePageInBook(int book_id, int pages) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + BOOKS_TABLE_NAME + " SET " + BOOKS_TABLE_COLUMN_PAGE + " = " + pages + " WHERE " + BOOKS_TABLE_COLUMN_ID + " = " + book_id + ";" ;
        db.execSQL(sql);
        return true;
    }

    public boolean finishBook(int book_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_TABLE_COLUMN_TYPE, BOOK_TYPE_ARCHIVE);
        contentValues.put(BOOKS_TABLE_COLUMN_END_DATE, getTodayDateString());
        contentValues.put(BOOKS_TABLE_COLUMN_END_YEAR, getYearString());
        db.update(BOOKS_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(book_id)});
        return true;
    }

    public Book getLastInsertedBook() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
        return new Book(
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE))
        );
    }

    public Book getBookByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BOOKS_TABLE_NAME, null, "id = '" + String.valueOf(id) + "'", null, null, null, null);
        cursor.moveToFirst();
        Book book =  new Book(
                id,
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_AUTHOR)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGES_ALL)),
                cursor.getInt(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_PAGE))
        );
        book.end_year = cursor.getString(cursor.getColumnIndex(BOOKS_TABLE_COLUMN_END_YEAR));
        return book;
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

    public String getYearString() {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy");
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
