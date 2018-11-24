package shoppinglistrealm.adi.shoppinlistrealm


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    //private var shoppingItems : RealmList<ShoppingItem> = RealmList<ShoppingItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        Realm.init(getApplicationContext());

        val realmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()


        //realmConfiguration.shouldDeleteRealmIfMigrationNeeded()

        // Open the realm for the UI thread.
        realm = Realm.getInstance(realmConfiguration)



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL //or orizontal
        shoppingListRecyclerView.layoutManager = layoutManager


        val adapter = ShoppingItemsAdapter(this, realm.where<ShoppingItem>().findAll())
        //val adapter = ShoppingItemsAdapter(this,shopppingItems)

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
                    startActivity(intent)

                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        logd("onResume was called")
        super.onResume()
        loadAll()
    }

    fun loadAll() {

        val notesAdapter = ShoppingItemsAdapter(this, realm.where<ShoppingItem>().findAll())
        shoppingListRecyclerView.adapter = notesAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close() // Remember to close Realm when done.
    }
}


