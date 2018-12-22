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

class ShoppingItemsAdapter(val context: Context) : RecyclerView.Adapter<ShoppingItemsAdapter.MyViewHolder>(){
    private lateinit var mRecyclerView: RecyclerView;
    var list: ArrayList<ShoppingItem2> = ArrayList()

    val client by lazy { ModelClientAPI.create() }

    init { refreshMovies() }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
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

    fun refreshMovies() {
        client.getItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    list.clear()
                    list.addAll(result)
                    notifyDataSetChanged()
                },
                { error ->
                    Toast.makeText(context, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                    logd("${error.message}")
                }
            )
    }


    inner class MyViewHolder(itemView :View): RecyclerView.ViewHolder(itemView){
        var currentItem : ShoppingItem2? = null
        var currentPosition: Int = 0

//        fun deleteItem(item: ShoppingItem) {
//            realm.executeTransaction { realm ->
//                currentItem!!.deleteFromRealm()
//            }
//
//        }
        init {

            itemView.setOnClickListener {
                //Toast.makeText(context,currentItem!!.title + "Clicked !", Toast.LENGTH_SHORT).show()
                val intent = Intent(context,ShoppingItemActivity::class.java)
                intent.putExtra("itemPosition",currentPosition)//asta e ca sa stiu ce element trebuie sa updatez in lista de supplier
                intent.putExtra("itemTitle", itemView.txvTitle.text.toString())
                intent.putExtra("itemQuantity", itemView.txvQuantity.text.toString())
                intent.putExtra("itemId",currentItem!!.id)
                context.startActivity(intent)
            }
            itemView.imgDelete.setOnClickListener{
//                realm.executeTransaction { realm ->
//                    //currentItem!!.deleteFromRealm()
//                    notifyDataSetChanged()
//                }

            }
            itemView.imgEdit.setOnClickListener{
                val intent = Intent(context,ShoppingItemActivity::class.java)
                intent.putExtra("itemPosition",currentPosition)//asta e ca sa stiu ce element trebuie sa updatez in lista de supplier
                intent.putExtra("itemTitle", itemView.txvTitle.text.toString())
                intent.putExtra("itemQuantity", itemView.txvQuantity.text.toString())
                intent.putExtra("itemId",currentItem!!.id)
                context.startActivity(intent)
                //notifyDataSetChanged() asta devine optional
            }
        }
        fun setData(item : ShoppingItem2?, pos: Int){
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

