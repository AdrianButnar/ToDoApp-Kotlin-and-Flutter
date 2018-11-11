import 'package:flutter/material.dart';
//import 'package:english_words/english_words.dart';
import 'package:redux/redux.dart';
import 'package:flutter_redux/flutter_redux.dart';

import 'package:shopping_flutter/model/model.dart';
import "package:shopping_flutter/redux/actions.dart";
import "package:shopping_flutter/redux/reducers.dart";

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final Store<AppState> store = Store<AppState>(
      appStateReducer,
      initialState: AppState.initialState(),
    );
    return StoreProvider(
      store : store,
      child: MaterialApp(
        title: 'Shopping List',
        home: MyHomePage(),
      )

    );
  }
}
class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {

    return Scaffold (
      appBar: AppBar(
        title: Text('Shopping List'),
      ),
      body: StoreConnector<AppState,_ViewModel>(
          //a function which convers the shopping into the viewmodel that we want to create
          converter: (Store<AppState> store) => _ViewModel.create(store),
          builder: (BuildContext context, _ViewModel viewModel) => Column(
            children: <Widget>[
              AddItemWidget(viewModel),
              Expanded(child: ItemListWidget(viewModel)),
              //RemoveItemsButton(viewModel),
            ],
           ),
          ),
    );
  }
}

class _ViewModel{
  final List<ShoppingItem> items;
  final Function(String,String) onAddItem;
  final Function(ShoppingItem) onRemoveItem;
  final Function(ShoppingItem,String) onEditItem;

  _ViewModel({
    this.items,
    this.onAddItem,
    this.onRemoveItem,
    this.onEditItem
});


  factory _ViewModel.create(Store<AppState> store){
    _onAddItem(String title,String quantity){
      //store.dispatch(AddItemAction(title, quantity));
      store.dispatch(AddItemAction(title));
    }

    _onRemoveItem(ShoppingItem item) {
      store.dispatch(RemoveItemAction(item));
    }

    _onEditItem(ShoppingItem item,String newTitle){
      store.dispatch(EditItemAction(item,newTitle));
    }

    return _ViewModel(
      items: store.state.shoppingItems,
      onAddItem: _onAddItem,
      onEditItem: _onEditItem,
      onRemoveItem: _onRemoveItem,
    );
  }
}

class AddItemWidget extends StatefulWidget {
  final _ViewModel model;

  AddItemWidget(this.model);

  @override
  _AddItemState createState() => _AddItemState();
}

class _AddItemState extends State<AddItemWidget> {
  final TextEditingController controller = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: controller,
      decoration: InputDecoration(
        hintText: ' Add an Item',
      ),
      onSubmitted: (String title) {
        widget.model.onAddItem(title,"");
        controller.text = '';
      },
    );
  }
}

class ItemListWidget extends StatelessWidget {
  final _ViewModel model;
  ItemListWidget(this.model);

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: model.items
          .map((ShoppingItem item) => ListTile(
        title: Text(item.title),
        trailing: IconButton(
            icon: Icon(Icons.edit),
            //onPressed: () => model.onEditItem(item,"textDinTextBOx")),
            onPressed: () => model.onEditItem(item,"textdinTextBox")),
        leading: IconButton(
          icon: Icon(Icons.delete),
          onPressed: () => model.onRemoveItem(item),
        )
      ))
          .toList(),
    );
  }
}

