import 'package:flutter/foundation.dart';
import 'package:shopping_flutter/utils/databaseHelpers.dart';
import 'package:http/http.dart' as http;


class ShoppingItem{
   int id;
   String title;
   String quantity;

  ShoppingItem({
    @required this.id,
    @required this.title,
    @required this.quantity
  });
////asta e nou de la db
   ShoppingItem.map(dynamic obj) {
     this.id = obj['id'];
     this.title = obj['title'];
     this.quantity = obj['quantity'];
   }
  Map<String, dynamic> toMap() {
    var map = new Map<String, dynamic>();
    if (id != null) {
      map['id'] = id;
    }
    map['title'] = title;
    map['quantity'] = quantity;

    return map;
  }

  Map<String, dynamic> toSuperMap() {
    var map = new Map<String, dynamic>();
    if (id != null) {
      map['id'] = id;
      map['id'] = id;
    }
    map['title'] = title;
    map['quantity'] = quantity;

    return map;
  }


  ShoppingItem.fromMap(Map<String, dynamic> map) {
    this.id = map['id'];
    this.title = map['title'];
    this.quantity = map['quantity'];
  }
   ////////////////////////////
  ShoppingItem copyWith({int id, String title, String body}){
    return ShoppingItem(
      id: id?? this.id,
      title: title ?? this.title,
      quantity: quantity
    );
  }

}

class AppState {
  List<ShoppingItem> shoppingItems;
  final DatabaseHelper db;
  AppState ({
    @required this.shoppingItems,
    @required this.db
  });
  //AppState.initialState():shoppingItems = List.unmodifiable(<ShoppingItem>[]);
  AppState.initialState(this.db, List<ShoppingItem> initialShoppingList) {
    shoppingItems = initialShoppingList;
    //loadDB();
  }
//  void loadDB() async{
//    shoppingItems = await db.getAllItems() as List<ShoppingItem>;
//  }
}