package adrian.planner

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: ShoppingItemsAdapter
    private var shoppingItems: MutableList<ShoppingItem> = mutableListOf()
    private lateinit var ref: DatabaseReference
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {

        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkTheme)
        else
            setTheme(R.style.AppTheme)

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        ref = FirebaseDatabase.getInstance().getReference("items");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL //or orizontal
        shoppingListRecyclerView.layoutManager = layoutManager


        val adapter = ShoppingItemsAdapter(this,shoppingItems)
        myAdapter = adapter
        shoppingListRecyclerView.adapter = adapter


        ref.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                shoppingItems.clear()
                for(child in p0.children){
                    val item = child.getValue(ShoppingItem::class.java)
                    shoppingItems.add(item!!)
                }
                myAdapter.notifyDataSetChanged()

            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        myAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
//        //theme changer
//        val switch : MenuItem = menu!!.findItem(R.id.app_bar_switch)
//        themeSwitch = menu!!.findItem(R.id.app_bar_switch).actionView as Switch
//        themeSwitch.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
//            if (b){
//                setTheme(R.style.darkTheme)
////                startActivity(Intent(MainActivity));
////                finish();
//            } else{
//                setTheme(R.style.AppTheme)
//            }
//        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addBtn -> {
                    val intent = Intent(this, ShoppingItemActivity::class.java)
                    startActivityForResult(intent,1)

                }
                R.id.app_bar_switch -> {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        //setTheme(R.style.AppTheme)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        //restart
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        //setTheme(R.style.darkTheme)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        logd("onResume was called")
        super.onResume()
        //myAdapter.notifyDataSetChanged()
        //loadAll()

    }
//     fun loadAll() {
//
//        val itemsAdapter = ShoppingItemsAdapter(this,realm.where<ShoppingItem>().findAll())
//        shoppingListRecyclerView.adapter = itemsAdapter
//    }


}
