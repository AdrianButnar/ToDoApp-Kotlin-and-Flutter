package adrian.planner

import android.annotation.SuppressLint
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_add_item.*
import com.google.firebase.database.DatabaseReference



class ShoppingItemActivity : AppCompatActivity() {
    private val ref = FirebaseDatabase.getInstance().getReference("items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {//asta inseamna ca vine din edit
            edtTitle.setText(bundle.getString("itemTitle"))
            edtQuantity.setText(bundle.getString("itemQuantity"))

        }

        btAdd.setOnClickListener {

            if(bundle == null) {

                //Supplier.shopppingItems.add(ShoppingItem(edtTitle.text.toString(), edtQuantity.text.toString()))

                val id = ref.push().key
                val item = ShoppingItem(id!!,edtTitle.text.toString(),edtQuantity.text.toString())

                ref.child(id).setValue(item).addOnCompleteListener{
                    Toast.makeText(applicationContext, "Item Successfully added",Toast.LENGTH_LONG).show()

                }

                finish()
            }
            else{
                val id = bundle.getString("itemId")
                val item = ShoppingItem(id!!,edtTitle.text.toString(),edtQuantity.text.toString())
                ref.child(id).setValue(item).addOnCompleteListener{
                    Toast.makeText(applicationContext, "Item Edited added",Toast.LENGTH_LONG).show()

                }
                finish()

            }
        }
    }
}
