package adrian.planner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        var shopppingItems = listOf<ShoppingItem>(
//            ShoppingItem("Potatoes"),
//            ShoppingItem("Tomatoes"),
//            ShoppingItem("Tomato Sauce"),
//            ShoppingItem("Milk"),
//            ShoppingItem("AA Batteries"),
//            ShoppingItem("Flowers"),
//            ShoppingItem("Mousepad")
//        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL //or orizontal
        shoppingListRecyclerView.layoutManager = layoutManager


        val adapter = ShoppingItemsAdapter(this,Supplier.shopppingItems)
        //val adapter = ShoppingItemsAdapter(this,shopppingItems)

        shoppingListRecyclerView.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addBtn -> {
                    val intent = Intent(this, ShoppingItemActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadAll()
    }
     fun loadAll() {

        val notesAdapter = ShoppingItemsAdapter(this, Supplier.shopppingItems)
        shoppingListRecyclerView.adapter = notesAdapter
    }
}
