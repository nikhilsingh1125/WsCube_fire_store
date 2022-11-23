package com.wscube_fire_store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.firestore.FirebaseFirestore
import com.wscube_fire_store.model.Notes

class NotesAddActivity : AppCompatActivity() {

    lateinit var activity: NotesAddActivity
    lateinit var db :FirebaseFirestore
    lateinit var edit :String
    lateinit var header :String
    lateinit var dece :String
    lateinit var id :String
    lateinit var title :EditText
    lateinit var context :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_add)

        activity = this
        db = FirebaseFirestore.getInstance()

        val intent = intent
        edit = intent.getStringExtra("isEdit").toString()
        id = intent.getStringExtra("id").toString()
        header = intent.getStringExtra("title").toString()
        dece = intent.getStringExtra("context").toString()

         title = findViewById<EditText>(R.id.edtTitle)
         context = findViewById<EditText>(R.id.edtDece)

        val back = findViewById<ImageView>(R.id.actbar_back)
        back.visibility = View.VISIBLE
        val act_title = findViewById<TextView>(R.id.actbar_title)
        act_title.text = "Add Notes"

        back.setOnClickListener {
            onBackPressed()
        }


        if (edit.equals("Y"))
        {
            title.text = Editable.Factory.getInstance().newEditable(header)
            context.text = Editable.Factory.getInstance().newEditable(dece)

        }

        findViewById<AppCompatButton>(R.id.btnSubmit).setOnClickListener {
            
            if (edit.equals("Y")){
                editValidation()
            }
            else
            {
                addValidation()
            }
           
           

           


        }

    }

    private fun editValidation() {
        if (title.text.toString().trim().equals("")){

            Toast.makeText(activity, "Please enter title", Toast.LENGTH_SHORT).show()
        }

        else if(context.text.toString().trim().equals("")){
            Toast.makeText(activity, "Please enter description", Toast.LENGTH_SHORT).show()
        }

        else
        {
            val noteTitle = title.text.toString()
            val noteDece = context.text.toString()

            updateDataToFirestore(noteTitle, noteDece);

        }
    }

    private fun updateDataToFirestore(noteTitle: String, noteDece: String) {

        val editMap = mapOf(
            "title" to noteTitle,
            "description" to noteDece
        )

        db.collection("Notes").document(id).update(editMap).addOnSuccessListener {

            startActivity(Intent(activity,MainActivity::class.java))
            Toast.makeText(activity, "Your note is updated", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun addValidation() {
        
        if (title.text.toString().trim().equals("")){

            Toast.makeText(activity, "Please enter title", Toast.LENGTH_SHORT).show()
        }

        else if(context.text.toString().trim().equals("")){
            Toast.makeText(activity, "Please enter description", Toast.LENGTH_SHORT).show()
        }

        else
        {
            val noteTitle = title.text.toString()
            val noteDece = context.text.toString()

            addDataToFirestore(noteTitle, noteDece);

        }
    }

    //Add data to FireStore
    private fun addDataToFirestore(noteTitle: String, noteDece: String) {

        val dbNotes = db.collection("Notes")

        val data = Notes(noteTitle,noteDece)


        dbNotes.add(data).addOnSuccessListener {

            db.collection("Notes").document(it.id).set(Notes(it.id, noteTitle,noteDece))

            println("test id"+it.id)

            Toast.makeText(activity, "Your Notes has been added to Firebase Firestore", Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity,MainActivity::class.java))
        }
            .addOnFailureListener {
            Toast.makeText(activity, "Fail to add note", Toast.LENGTH_SHORT).show();
        }

    }
}