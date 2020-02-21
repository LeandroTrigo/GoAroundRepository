package e.utilizador.around;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;


public class DatabaseHelper extends SQLiteOpenHelper {
    //Declaração da base de dados
    public static final String DATABASE_NAME = "database.db";


    //Declaração das tabelas e colunas
    public static final String TABLE_NAME ="notas";
    public static final String COL_1 ="Id";
    public static final String COL_2 ="Descricao";
    public static final String COL_3 = "Data";

    //Construtor da Base de Dados
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    //Criação das tabelas com os respetivos atributos onde o id é auto incrementado.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE notas (ID INTEGER PRIMARY  KEY AUTOINCREMENT, Descricao TEXT, Data DATE)");
    }



    //Metodo para substituir uma tabela se a mesma for atualizada.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    //Método para adicionar uma Nota
    public long addNota(String descricao, String data){
        //Obtemos a Database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Atribuimos valores às colunas (Menos ao id que é auto incrementado)
        contentValues.put(COL_2,descricao);
        contentValues.put(COL_3,data);
        //Inserirmos esses valores na base de dados.
        long res = db.insert("notas",null,contentValues);
        db.close();
        Log.d("INSERIDO", "INSERIDO COM ID: " +res);
        return  res;

    }

    //Metodo para listar notas
    public Cursor getNotas() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    //Metodo para apagar nota
    public void deleteNota(String des){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_1 + " = '" + des;
        System.out.println(query);
        db.execSQL(query);
    }

}