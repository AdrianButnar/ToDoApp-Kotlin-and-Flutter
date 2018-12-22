package adrian.planner

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.list_item.view.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.RealmList
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity


class ShoppingItemsAdapter(val context: Context) : RecyclerView.Adapter<ShoppingItemsAdapter.MyViewHolder>(){

    private var realm: Realm = Realm.getDefaultInstance()

    private lateinit var mRecyclerView: RecyclerView;
    var list: RealmList<ShoppingItem> = RealmList();
    val syncList: RealmList<ShoppingItem> = RealmList()
    val client by lazy { ModelClientAPI.create() }

    init { refreshMovies() }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    fun verifyAvailableNetwork(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        //val view = LayoutInflater.from(context).inflate(R.layout.new_list_item, parent, false);
        return MyViewHolder(view);
    }



    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shoppingItem = list[position]
        holder.setData(shoppingItem,position)
        logd("reached onBindViewHolder on position: $position")
    }

    fun addItemToServer(item: ShoppingItem) {
        client.addItem(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshMovies() }, { throwable ->
                Toast.makeText(context, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            )
    }

    fun deleteItemToServer(item: ShoppingItem) {

        client.deleteItem(item.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshMovies() }, { throwable ->
                Toast.makeText(context, "Delete error: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            )

    }
    fun updateItemOnServer(item: ShoppingItem) {
        client.updateItem(item.id, item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshMovies() }, { throwable ->
                Toast.makeText(context, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
            }
            )
    }

    fun refreshLocal(){
        val realmResultList : RealmResults<ShoppingItem> = realm.where<ShoppingItem>().findAll()
        list.clear()
        list.addAll(realmResultList.subList(0, realmResultList.size))
    }
    fun refreshMovies() {
        if (verifyAvailableNetwork(context))
            client.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
            //                    list.clear()
            //                    list.addAll(result)
                        realm.executeTransaction({ realm ->
                            val listRealm : RealmList<ShoppingItem> = RealmList()
                            listRealm.addAll(result)
                            realm.deleteAll()
                            realm.insertOrUpdate(listRealm)
                            notifyDataSetChanged()
                        })
                        val realmResultList : RealmResults<ShoppingItem> = realm.where<ShoppingItem>().findAll()
                        list.clear()
                        list.addAll(realmResultList.subList(0, realmResultList.size))
                        logd("INTERNET" + verifyAvailableNetwork(context));
                    },
                    { error ->
                        Toast.makeText(context, "Refresh error, you are offline: ${error.message}", Toast.LENGTH_LONG).show()
                        logd("${error.message}")
                        logd("INTERNET" + verifyAvailableNetwork(context));


                    }
                )
        else {
            Toast.makeText(context, "Refresh error, you are offline", Toast.LENGTH_LONG).show()
            val realmResultList: RealmResults<ShoppingItem> = realm.where<ShoppingItem>().findAll()
            list.clear()
            list.addAll(realmResultList.subList(0, realmResultList.size))
        }
    }

    fun syncRealmWithServer() {
        for (item : ShoppingItem in syncList){
            //Toast.makeText(context,"title + ${item.id} ${item.title} + ${item.quantity}", Toast.LENGTH_SHORT).show()
            addItemToServer(ShoppingItem(item.id,item.title,item.quantity)); //pentru ca are request body
        }
        syncList.clear()
        //Toast.makeText(context,"Welcome back online.Server synced with local database", Toast.LENGTH_SHORT).show()

    }


    inner class MyViewHolder(itemView :View): RecyclerView.ViewHolder(itemView){
        var currentItem : ShoppingItem? = null
        var currentPosition: Int = 0

        init {

            itemView.setOnClickListener {
                if (verifyAvailableNetwork(context)) {
                    syncRealmWithServer()
                    //Toast.makeText(context,currentItem!!.title + "Clicked !", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ShoppingItemActivity::class.java)
                    intent.putExtra(
                        "itemPosition",
                        currentPosition
                    )//asta e ca sa stiu ce element trebuie sa updatez in lista de supplier
                    intent.putExtra("itemTitle", itemView.txvTitle.text.toString())
                    intent.putExtra("itemQuantity", itemView.txvQuantity.text.toString())
                    intent.putExtra("itemId", currentItem!!.id)
                    context.startActivity(intent)
                }
                else{
                    Toast.makeText(context, "This action cannot be done offline", Toast.LENGTH_LONG).show()
                }
            }
            itemView.imgDelete.setOnClickListener{
                if (verifyAvailableNetwork(context)){
                    syncRealmWithServer()
                    realm.executeTransaction { realm ->
                        deleteItemToServer(currentItem!!)
                        //currentItem!!.deleteFromRealm()
                        notifyDataSetChanged()
                    }
                }
                else{
                    Toast.makeText(context, "This action cannot be done offline", Toast.LENGTH_LONG).show()

                }

            }
            itemView.imgEdit.setOnClickListener{
                if (verifyAvailableNetwork(context)) {
                    syncRealmWithServer()
                    val intent = Intent(context, ShoppingItemActivity::class.java)
                    intent.putExtra(
                        "itemPosition",
                        currentPosition
                    )//asta e ca sa stiu ce element trebuie sa updatez in lista de supplier
                    intent.putExtra("itemTitle", itemView.txvTitle.text.toString())
                    intent.putExtra("itemQuantity", itemView.txvQuantity.text.toString())
                    intent.putExtra("itemId", currentItem!!.id)
                    context.startActivity(intent)
                }
                else{
                    Toast.makeText(context, "This action cannot be done offline", Toast.LENGTH_LONG).show()
                }
            }
        }
        fun setData(item : ShoppingItem?, pos: Int){
            itemView.txvTitle.text = item!!.title
            itemView.txvQuantity.text = item.quantity
            currentItem = item
            currentPosition = pos
            logd("set data on position: $pos")

        }
//        fun setData(item: ShoppingItem?, pos:Int){
//            itemView.tvTitle.text= item!!.title
//            itemView.tvContent.text = item.quantity
//            currentItem = item
//            currentPosition = pos
//        }
    }

}

