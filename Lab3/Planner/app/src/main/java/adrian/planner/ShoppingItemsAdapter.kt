package adrian.planner

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.list_item.view.*

class ShoppingItemsAdapter(val context: Context, var shoppingItems: MutableList<ShoppingItem>) : RecyclerView.Adapter<ShoppingItemsAdapter.MyViewHolder>(){
    private lateinit var mRecyclerView: RecyclerView;

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
        return shoppingItems.size;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.setData(shoppingItem,position)
        logd("reached onBindViewHolder on position: $position")
    }



    inner class MyViewHolder(itemView :View): RecyclerView.ViewHolder(itemView){
        var currentItem : ShoppingItem? = null
        var currentPosition: Int = 0


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
//                shoppingItems.remove(currentItem);
//                notifyDataSetChanged()
                val ref = FirebaseDatabase.getInstance().getReference("items")
                ref.child(currentItem!!.id).removeValue().addOnCompleteListener {
                    Toast.makeText(context,"Item Sucessfully deleted", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged()
                }

            }
            itemView.imgEdit.setOnClickListener{
                val intent = Intent(context,ShoppingItemActivity::class.java)
                intent.putExtra("itemPosition",currentPosition)//asta e ca sa stiu ce element trebuie sa updatez in lista de supplier
                intent.putExtra("itemTitle", itemView.txvTitle.text.toString())
                intent.putExtra("itemQuantity", itemView.txvQuantity.text.toString())
                intent.putExtra("itemId",currentItem!!.id)
                context.startActivity(intent)

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

