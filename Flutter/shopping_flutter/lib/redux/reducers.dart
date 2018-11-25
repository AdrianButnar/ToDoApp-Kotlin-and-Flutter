import 'package:shopping_flutter/model/model.dart';
import 'package:shopping_flutter/redux/actions.dart';

AppState appStateReducer(AppState state, action){
  return AppState(
      shoppingItems: itemReducer(state.shoppingItems, action)
  );
}

List<ShoppingItem> itemReducer(List<ShoppingItem> state,action){
  if(action is AddItemAction){
    return []
      ..addAll(state)
      ..add(ShoppingItem(id: action.id, title: action.title, quantity: action.quantity));
    //..add(ShoppingItem(id: action.id, title: action.title, quantity: action.quantity));
  }

  if(action is RemoveItemAction){
    return List.unmodifiable(List.from(state)..remove(action.item));
  }

  if (action is EditItemAction){
    int index = List.from(state).indexOf(action.item);
    int id = action.item.id;
    action.item.quantity = action.newQuantity;
    action.item.title = action.newTitle;

    //return List.unmodifiable((List.from(state)..remove(action.item))..insert(index, ShoppingItem(id: id, title: action.newTitle, quantity: "")));
    //return List.unmodifiable((List.from(state)..remove(action.item))..insert(index, ShoppingItem(id: id, title: action.newTitle,quantity: action.newQuantity)));
    return List.from(state);

  }
  return state;
}