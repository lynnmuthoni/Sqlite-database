package com.king.morningsqlitedatabaseapp

import android.content.Context
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var editTextName:EditText?=null
    var editTextEmail:EditText?=null
    var editTextIdNumber:EditText?=null
    var buttonSave:Button?=null
    var buttonView:Button?=null
    var buttonDelete:Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextName=findViewById(R.id.edtName)
        editTextEmail=findViewById(R.id.edtemail)
        editTextIdNumber=findViewById(R.id.edtNumber)
        buttonSave=findViewById(R.id.mBtnSave)
        buttonView=findViewById(R.id.mBtnView)
        buttonDelete=findViewById(R.id.mBtnDelete)

        //Create a database
        var db =openOrCreateDatabase("votersDB",Context.MODE_PRIVATE,null)
        //Create a table called users inside the votersDB
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR,arafa VARCHAR,kitambulisho VARCHAR)")
        //Set a listener to implement the saving
        buttonSave!!.setOnClickListener {
            //Receive data from user
            var userName=editTextName!!.text.toString()
            var userEmail=editTextEmail!!.text.toString()
            var userIdNumber=editTextIdNumber!!.text.toString()
            //Check if the user is submitting empty fields
            if (userName.isEmpty()|| userEmail.isEmpty()||userIdNumber.isEmpty()){
                messages("EMPTY FIELDS!!","Please fill in all inputs!!")
            }else{
                //Proceed to save the data
                db.execSQL("INSERT INTO users VALUES('"+userName+"','"+userEmail+"','"+userIdNumber+"')")
                messages("SUCCESS!!!","Data saved successfully")
                clear()
            }
        }

        buttonView!!.setOnClickListener {
            //Use cursor to select all the data from the database
            var cursor=db.rawQuery("SELECT*FROM users",null)
            //Check if there is no record in the db
            if (cursor.count ==0){
                messages("NO DATA!!!","Sorry, the db is empty!!")
            }else{
                //Use a string buffer to append all records for display
                var buffer=StringBuffer()
                //use a loop to display data per row
                while (cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n")//Column 0 is for name
                    buffer.append(cursor.getString(1)+"\n")//Column 1 is for Email
                    buffer.append(cursor.getString(2)+"\n\n")//Column 2 is for ID No
                }
                messages("DB RECORDS",buffer.toString())
            }
        }
        buttonDelete!!.setOnClickListener {
            //receive the ID number from the user
            var idNumber= editTextIdNumber!!.text.toString().trim()
            //Check if the user is submitting an empty field
            if (idNumber.isEmpty()){
                messages("EMPTY FIELD!!","Please enter ID no!!!")
            }else{
                //Use cursor to select user with provided ID
                var cursor =db.rawQuery("SELECT* FROM users WHERE kitambulisho='"+idNumber+"'",null)
                //Check if there is a user provided id
                if (cursor.count==0){
                    messages("NO RECORD!!!","No user found")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                    messages("SUCCESS!!!","Record deleted successfully!!!")
                    clear()
                }
            }
        }
    }
    fun messages (title:String, message:String){
        var alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.create().show()
    }

    fun clear(){
        editTextName!!.setText("")
        editTextEmail!!.setText("")
        editTextIdNumber!!.setText("")
    }
}