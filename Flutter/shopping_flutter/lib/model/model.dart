import 'package:flutter/foundation.dart';

class ShoppingItem{
   int id;
   String title;
   String quantity;

  ShoppingItem({
    @required this.id,
    @required this.title,
    @required this.quantity
  });


  ShoppingItem copyWith({int id, String title, String body}){
    return ShoppingItem(
      id: id?? this.id,
      title: title ?? this.title,
      quantity: quantity
    );
  }
}

class AppState{
  final List<ShoppingItem> shoppingItems;
  AppState({
    @required this.shoppingItems
  });
  AppState.initialState():shoppingItems = List.unmodifiable(<ShoppingItem>[]);
}