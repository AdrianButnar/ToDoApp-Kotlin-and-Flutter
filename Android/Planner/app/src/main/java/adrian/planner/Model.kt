package adrian.planner

import android.support.annotation.IntegerRes
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

//@RealmClass
open class ShoppingItem(@PrimaryKey var id: Int = 0, var title: String="",var quantity: String="" ) : RealmObject(){}

//object Supplier {
//    var shopppingItems = RealmList<ShoppingItem>(
////        ShoppingItem("Potatoes","1 kg"),
////        ShoppingItem("Tomatoes","2 kg"),
////        ShoppingItem("Tomato Sauce","500 ml"),
////        ShoppingItem("Milk","3 litres"),
////        ShoppingItem("AA Batteries","4"),
////        ShoppingItem("Flowers","5 roses"),
////        ShoppingItem("Mousepad","UBB")
//    )
//open class Supplier(
//    var shoppingItems : RealmList<ShoppingItem> = RealmList<ShoppingItem>()
//
//)
data class ShoppingItem2(@PrimaryKey var id: Int = 0, val title: String, val quantity: String )
data class ShoppingItemList (
    @SerializedName("items" )
    val items: List<ShoppingItem2>
)
data class ShoppingItemEmbedded (
    @SerializedName("_embedded" )
    val list: ShoppingItemList
)
