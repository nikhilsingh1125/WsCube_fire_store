package com.wscube_fire_store

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.auth.User
import com.wscube_fire_store.adapter.RecyclerViewAdapter
import com.wscube_fire_store.model.Notes
import libs.mjn.prettydialog.PrettyDialog
import libs.mjn.prettydialog.PrettyDialogCallback

class MainActivity : AppCompatActivity() {

    lateinit var activity: MainActivity
    lateinit var arrNotes :ArrayList<Notes>
    lateinit var db :FirebaseFirestore
    lateinit var rvAdapter: RecyclerViewAdapter
    lateinit var llHome : LinearLayout
    lateinit var loader: LottieAnimationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this

        db = FirebaseFirestore.getInstance()

        arrNotes = arrayListOf()

        val add = findViewById<FloatingActionButton>(R.id.btnAdd)
        loader = findViewById(R.id.lottieLoader)
        llHome = findViewById(R.id.llHome)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        rvAdapter = RecyclerViewAdapter(activity,arrNotes)

        loader.setAnimation(R.raw.loader)
        loader.visibility = View.VISIBLE
        loader.playAnimation()

        db.collection("Notes").get()
            .addOnSuccessListener {

                if (!it.isEmpty) {

                    for (data in it.documents){

                        System.out.println("test123 data"+it.documents)
                        val note : Notes? = data.toObject(Notes::class.java)
                        if (note != null) {
                            arrNotes.add(note)

                            loader.visibility = View.GONE
                            recyclerView.visibility =View.VISIBLE


                            rvAdapter = RecyclerViewAdapter(activity,arrNotes)
                            val layoutManager = LinearLayoutManager(activity)
                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = rvAdapter
                        }
                        else {
                            recyclerView.visibility =View.GONE
                            llHome.visibility = View.VISIBLE
                        }
                    }

                    rvAdapter.notifyDataSetChanged()

                }
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
            }


        add.setOnClickListener {
            startActivity(Intent(activity,NotesAddActivity::class.java))
        }

    }

    fun setDelete(position: Int) {

        showAlert(activity,position)

    }

    fun showAlert(context: Context?, position: Int) {
        val pDialog = PrettyDialog(context)
        pDialog
            .setTitle(getString(R.string.app_name))
            .setMessage("You want to delete this note")
            .setIcon(libs.mjn.prettydialog.R.drawable.pdlg_icon_info)
            .setIconTint(R.color.blue_light)
            .addButton(
                "OK",  // button text
                R.color.white,  // button text color
                R.color.blue_light,  // button background color
                object : PrettyDialogCallback {
                    // button OnClick listener
                    override fun onClick() {
                        pDialog.dismiss()

                        db.collection("Notes").document(arrNotes.get(position).id!!).delete().addOnSuccessListener {

                            arrNotes.removeAt(position)
                            rvAdapter.notifyItemRemoved(position)
                            rvAdapter.notifyDataSetChanged()
                            Toast.makeText(activity, "Note deleted..", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                        }

                    }
                }
            )
            .addButton(
                "Cancel",  // button text
                R.color.white,  // button text color
                R.color.blue_light,  // button background color
                object : PrettyDialogCallback {
                    // button OnClick listener
                    override fun onClick() {
                        pDialog.dismiss()
                    }
                }
            )
            .show()
    }
}