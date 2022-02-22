package junaid.aslam.laindain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by M.Junaid Aslam on 02-01-2015.
 */
public class MyDatabaseHandler extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "PersonDB";
    private static String TABLE_PERSON = "persons";

    public MyDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

     public MyDatabaseHandler(Context context, int DATABASE_VERSION, String DATABASE_NAME, String TABLE_PERSON) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
         this.DATABASE_VERSION = DATABASE_VERSION;
         this.DATABASE_NAME = DATABASE_NAME;
         this.TABLE_PERSON = TABLE_PERSON;
     }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PERSON_TABLE = "create table "+TABLE_PERSON+"("+
                "name text, "+
                "amount DOUBLE, "+
                "status text, " +
                "date text)";
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PERSON);
        this.onCreate(db);
    }

    private static final String KEY_NAME = "name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";
    private static final String[] COLUMNS = {KEY_NAME, KEY_AMOUNT, KEY_STATUS, KEY_DATE};

    public void addPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, person.getName());
        values.put(KEY_AMOUNT, person.getAmount());
        values.put(KEY_STATUS, person.getStatus());
        values.put(KEY_DATE, person.getDate());
        db.insert(TABLE_PERSON, null, values);
        db.close();
    }

    public Person getPerson(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, COLUMNS, KEY_NAME + "=?", new String[]{String.valueOf(name)},
                null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();
        Person person = new Person();
        person.setName(cursor.getString(0));
        person.setAmount(cursor.getString(1));
        person.setStatus(cursor.getString(2));
        person.setDate(cursor.getString(3));
        db.close();
        return person;
    }




    public List<MyCostumRow> getAllPersons() {

        List<MyCostumRow> persons= new LinkedList<MyCostumRow>();
        String query= "select * from "+TABLE_PERSON;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query, null);

        MyCostumRow person= null;

        if(cursor.moveToLast()) {
            do{
                person= new MyCostumRow();
                person.setName(cursor.getString(0));
                person.setAmount(cursor.getString(1));
                person.setStatus(cursor.getString(2));
                person.setDate(cursor.getString(3));
                persons.add(person);
            } while(cursor.moveToPrevious());
        }
        return persons;
    }

    public List<String> getAllPersonsList() {

        List<String> persons= new LinkedList<String>();
        String query= "select * from "+TABLE_PERSON;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor= db.rawQuery(query, null);
        String person= null;
        if(cursor.moveToLast()) {
            do{
                person= new String();
                person = cursor.getString(0);//(Integer.valueOf(cursor.getString(0)));
                persons.add(person);
            } while(cursor.moveToPrevious());
        }
        return persons;
    }

    public int updatePerson(Person person) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("name", person.getName());
        values.put("amount", person.getAmount());
        values.put("status", person.getStatus());
        values.put("date", person.getDate());
        int i= db.update(TABLE_PERSON, values, KEY_NAME+"=?", new String[] {person.getName()});
        db.close();
        return i;
    }

    public void deletePerson(String name) {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_PERSON, KEY_NAME+" =?", new String[] {name});
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        this.onCreate(db);
    }

}