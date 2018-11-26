import 'package:redux/redux.dart';
import 'package:shopping_flutter/model/model.dart';
import 'package:shopping_flutter/redux/actions.dart';
import 'package:shopping_flutter/utils/databaseHelpers.dart';
import 'package:rxdart/futures.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux_thunk/redux_thunk.dart';

AppState appStateReducer(AppState state, action)  {
  return AppState(
      shoppingItems: itemReducer(state, action),
      db: state.db
  );
}
List<ShoppingItem> itemReducer(AppState state, action)  {
  if(action is AddItemAction){
    state.shoppingItems.add(ShoppingItem(id: action.id, title: action.title, quantity: action.quantity));
    return List.from(state.shoppingItems);

   state.db.saveItem(ShoppingItem(id: action.id, title: action.title, quantity: action.quantity));
   // List elems = await state.db.getAllItems();
    //return state.shoppingItems;
    ThunkAction<AppState> faChestii = (Store<AppState> store) async{
      var db;
      var id;
      var title;
      var quantity;
      Future result = db.saveItem(ShoppingItem(id: id, title: title, quantity: quantity));
      result.whenComplete(() async {
        List elems = await db.getAllItems();
        List<ShoppingItem> newList = new List<ShoppingItem>();
        elems.forEach((item)=>(newList.add(ShoppingItem.fromMap(item))));
        return newList;
      });
    };
    //
//
//    //faMishmashuri(db,state);
//    return await db.getAllItems();
    //..add(ShoppingItem(id: action.id, title: action.title, quantity: action.quantity));


  if(action is RemoveItemAction){
    return List.unmodifiable(List.from(state.shoppingItems)..remove(action.item));
  }
//
  if (action is EditItemAction) {
    int index = List.from(state.shoppingItems).indexOf(action.item);
    int id = action.item.id;
    action.item.quantity = action.newQuantity;
    action.item.title = action.newTitle;

    //return List.unmodifiable((List.from(state)..remove(action.item))..insert(index, ShoppingItem(id: id, title: action.newTitle, quantity: "")));
    //return List.unmodifiable((List.from(state)..remove(action.item))..insert(index, ShoppingItem(id: id, title: action.newTitle,quantity: action.newQuantity)));
    return List.from(state.shoppingItems);
  }



}
