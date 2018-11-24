package shoppinglistrealm.adi.shoppinlistrealm


import android.annotation.SuppressLint
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_add_item.*

class ShoppingItemActivity : AppCompatActivity() {
    private var realm: Realm = Realm.getDefaultInstance()

    var  id = 0

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

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
                id++
                //Supplier.shopppingItems.add(ShoppingItem(edtTitle.text.toString(), edtQuantity.text.toString()))
                realm.executeTransaction { realm ->
                    val item = realm.createObject<ShoppingItem>(id)
                    item.title = edtTitle.text.toString()
                    item.quantity = edtQuantity.text.toString()
                }
                finish()
            }
            else{
//                //modifica in lista de supplier elements current
//                shoppingItems[bundle.getInt("itemPosition")]!!.title =  edtTitle.text.toString()
//                shoppingItems[bundle.getInt("itemPosition")]!!.quantity =  edtQuantity.text.toString()
                finish()
            }
        }
    }
}
