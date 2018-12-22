package adrian.planner

import android.content.Intent
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.kotlin.delete
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
//import net.openid.appauth.AuthState



class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: ShoppingItemsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL //or orizontal
        shoppingListRecyclerView.layoutManager = layoutManager

        val adapter = ShoppingItemsAdapter(this)
        myAdapter = adapter

        shoppingListRecyclerView.adapter = adapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addBtn -> {
                    val intent = Intent(this, ShoppingItemActivity::class.java)
                    startActivityForResult(intent, -1)

                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        myAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        logd("onResume was called")
        super.onResume()
        myAdapter.notifyDataSetChanged();
        //loadAll()
    }

    fun loadAll() {

        val itemsAdapter = ShoppingItemsAdapter(this)
        shoppingListRecyclerView.adapter = itemsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
