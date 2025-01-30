import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val etWord = findViewById<EditText>(R.id.etWord)
        val btnTranslate = findViewById<Button>(R.id.btnTranslate)
        val tvTranslation = findViewById<TextView>(R.id.tvTranslation)

        // Добавление тестовых данных в базу данных
        dbHelper.insertTranslation("hello", "привет")
        dbHelper.insertTranslation("world", "мир")

        btnTranslate.setOnClickListener {
            val word = etWord.text.toString().trim()
            if (word.isNotEmpty()) {
                val translation = dbHelper.getTranslation(word)
                if (translation != null) {
                    tvTranslation.text = translation
                } else {
                    Toast.makeText(this, "Перевод не найден", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Введите слово", Toast.LENGTH_SHORT).show()
            }
        }
    }
}