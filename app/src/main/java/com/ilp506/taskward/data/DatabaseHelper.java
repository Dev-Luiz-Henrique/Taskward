package com.ilp506.taskward.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ilp506.taskward.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskward.db";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Iniciando a criação do banco de dados.");
        executeSQLFromFile(db, R.raw.create);
        executeSQLFromFile(db, R.raw.insert);
        Log.d("DatabaseHelper", "Banco de dados criado com sucesso.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // ...
    }

    private void executeSQLFromFile(SQLiteDatabase db, int resourceId) {
        try (InputStream inputStream = context.getResources().openRawResource(resourceId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                    continue;
                }

                sqlBuilder.append(line);
                if (line.endsWith(";")) {
                    String sql = sqlBuilder.toString();
                    Log.d("DatabaseHelper", "Executando SQL: " + sql);
                    if (sql.startsWith("SELECT")) {
                        // Para SELECT, usar rawQuery
                        Cursor cursor = db.rawQuery(sql, null);
                        // Processar o resultado, se necessário
                        cursor.close();
                    } else {
                        // Para comandos como INSERT, UPDATE, CREATE, etc., usar execSQL
                        db.execSQL(sql);
                    }
                    sqlBuilder.setLength(0); // Limpar o StringBuilder para o próximo comando
                }
            }
        } catch (IOException e) {
            Log.e("DatabaseHelper", "Erro ao ler o arquivo SQL.", e);
            throw new RuntimeException("Erro ao ler o arquivo SQL.", e);
        }
    }

}