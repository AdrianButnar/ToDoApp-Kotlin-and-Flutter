package adrian.planner

import android.annotation.SuppressLint
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_item.*

class ShoppingItemActivity : AppCompatActivity() {
    var  id = 0
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
                Supplier.shopppingItems.add(ShoppingItem(edtTitle.text.toString(), edtQuantity.text.toString()))
                finish()
            }
            else{
                //modifica in lista de supplier elements current
                Supplier.shopppingItems[bundle.getInt("itemPosition")].title =  edtTitle.text.toString()
                Supplier.shopppingItems[bundle.getInt("itemPosition")].quantity =  edtQuantity.text.toString()
                finish()
            }
        }
    }
}
