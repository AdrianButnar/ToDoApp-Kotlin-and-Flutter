import 'package:shopping_flutter/model/model.dart';

class AddItemAction{
  static int _id = 0;
  final String title;
  final String quantity;

  AddItemAction(this.title,this.quantity) {
    _id++;
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
  EditItemAction(this.item,this.newTitle);
}