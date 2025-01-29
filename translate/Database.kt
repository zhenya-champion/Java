import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Translator.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "translations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_TRANSLATION = "translation"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_WORD TEXT,"
                + "$COLUMN_TRANSLATION TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTranslation(word: String, translation: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_WORD, word)
        contentValues.put(COLUMN_TRANSLATION, translation)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getTranslation(word: String): String? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_TRANSLATION),
            "$COLUMN_WORD=?", arrayOf(word), null, null, null
        )
        return if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRANSLATION))
        } else {
            null
        }
    }
}