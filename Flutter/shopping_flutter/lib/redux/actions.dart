import 'package:redux/redux.dart';
import 'package:redux_thunk/redux_thunk.dart';
import 'package:shopping_flutter/model/model.dart';
import 'package:shopping_flutter/utils/databaseHelpers.dart';

class AddItemAction<AppState>{
  static int _id = 0;
  String title;
  final String quantity;

//  AddItemAction(this.title,this.quantity) {
//    _id++;
//  }

  AddItemAction(this.title,this.quantity) {

   _id++;

//ThunkAction<AppState> saveItem = (Store<AppState> store) async{
//  db.saveItem(ShoppingItem(id: this.id, title: this.title, quantity: this.quantity));
//  List elems = await db.getAllItems();
//  List<ShoppingItem> newList = new List<ShoppingItem>();
//  elems.forEach((item)=>(newList.add(ShoppingItem.fromMap(item))));
//  return newList;
//};

  }



  int get id => _id;

}
class RemoveItemAction{
  final ShoppingItem item;
  RemoveItemAction(this.item);
}

class EditItemAction{
  final ShoppingItem item;
  final String newTitle;
  final String newQuantity;
  EditItemAction(this.item,this.newTitle,this.newQuantity);
}

