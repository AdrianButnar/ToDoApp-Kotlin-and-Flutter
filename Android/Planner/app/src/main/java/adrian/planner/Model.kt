package adrian.planner

data class ShoppingItem(var title: String,var quantity: String)

object Supplier {
    var shopppingItems = mutableListOf<ShoppingItem>(
        ShoppingItem("Potatoes","1 kg"),
        ShoppingItem("Tomatoes","2 kg"),
        ShoppingItem("Tomato Sauce","500 ml"),
        ShoppingItem("Milk","3 litres"),
        ShoppingItem("AA Batteries","4"),
        ShoppingItem("Flowers","5 roses"),
        ShoppingItem("Mousepad","UBB")
    )
}