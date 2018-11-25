import 'package:flutter/material.dart';
//import 'package:english_words/english_words.dart';
import 'package:redux/redux.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'dart:async';

import 'package:shopping_flutter/model/model.dart';
import "package:shopping_flutter/redux/actions.dart";
import "package:shopping_flutter/redux/reducers.dart";
import 'package:shopping_flutter/utils/databaseHelpers.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:redux_thunk/redux_thunk.dart';
Future main() async{

  //await db.saveNote(new ShoppingItem("Flutter Tutorials"));
  var db = new DatabaseHelper();
  db.initDb();
  db.deleteItem(1);
  List initialShoppingList = await db.getAllItems();
  //db.saveItem(ShoppingItem(id: -2, title: "HARDCODED", quantity: "ITEM"));
  List<ShoppingItem> newList = new List<ShoppingItem>();
  initialShoppingList.forEach((item)=>(newList.add(ShoppingItem.fromMap(item))));
  //initialShoppingList.add(ShoppingItem(id: -2, title: "baiatul", quantity: "peste"));
  runApp(MyApp(db,newList));
}

class MyApp extends StatefulWidget {
  final List<ShoppingItem> initialShoppingList;
  final DatabaseHelper db;
  MyApp(this.db, this.initialShoppingList);

  @override
  _MyAppState createState() => _MyAppState(db,initialShoppingList);

}
class _MyAppState extends State<MyApp> {
  final DatabaseHelper db;
  final List<ShoppingItem> initialShoppingList;
  _MyAppState(this.db, this.initialShoppingList);
  var flag=true;
  Store<AppState> myStore=null;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    final Store<AppState> store = Store<AppState>(
        appStateReducer,
        initialState: AppState.initialState(db,initialShoppingList),
      middleware: [thunkMiddleware]
    );
    while(store.state.shoppingItems==null){
      setState(() {
        flag=false;
        myStore = store;

      });
    }
  }
  @override
  Widget build(BuildContext context) {
    if (flag == true) {
      return StoreProvider(
          store: myStore,
          child: MaterialApp(
            title: 'Shopping List',
            home: MyHomePage(db),
          )
      );
    }
    else{
      return new Container();
    }
  }
}
class EditScreen extends StatelessWidget {
  final _ViewModel model;
  final ShoppingItem item;
  EditScreen(this.model, this.item);


  @override
  Widget build(BuildContext context) {
    final myController1 = TextEditingController();
    final myController2 = TextEditingController();
    return Scaffold(
      appBar: AppBar(
        title: Text("Add Screen"),
      ),
      body: Center(
        child: Column(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    //padding: const EdgeInsets.only(bottom: 8.0),
                    child: TextField(
                      controller: myController1,
                      style: TextStyle(
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                      decoration: InputDecoration(
                        hintText: 'Title',
                      ),
                    ),
                  ),
                  TextField(
                    controller: myController2,
                    style: TextStyle(
                      color: Colors.black,
                    ),
                    decoration: InputDecoration(
                        hintText: "Quantity"
                    ),
                  ),
                ],
              ),
            ),
            RaisedButton(
              child: const Text("Edit"),
              onPressed: () {
                //AddItemWidget(myController1.text,myController2.text);
                model.onEditItem(item,myController1.text,myController2.text);
                Navigator.pop(context);
              },
            ),
          ],
        ),

//        child: [RaisedButton(
//          onPressed: () {
//            Navigator.pop(context);
//          },



      ),
    );
  }
}


class SecondScreen extends StatelessWidget {
  final _ViewModel model;
  SecondScreen(this.model);


  @override
  Widget build(BuildContext context) {
    final myController1 = TextEditingController();
    final myController2 = TextEditingController();
    return Scaffold(
      appBar: AppBar(
        title: Text("Add Screen"),
      ),
      body: Center(
        child: Column(
          children: [
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    //padding: const EdgeInsets.only(bottom: 8.0),
                    child: TextField(
                      controller: myController1,
                      style: TextStyle(
                        color: Colors.black,
                        fontWeight: FontWeight.bold,
                      ),
                      decoration: InputDecoration(
                        hintText: 'Title',
                      ),
                    ),
                  ),
                  TextField(
                    controller: myController2,
                    style: TextStyle(
                        color: Colors.black,
                    ),
                    decoration: InputDecoration(
                      hintText: "Quantity"
                    ),
                  ),
                ],
              ),
            ),
            RaisedButton(
              child: const Text("Add"),
              onPressed: () {
                //AddItemWidget(myController1.text,myController2.text);
                model.onAddItem(myController1.text,myController2.text);
                Navigator.pop(context);
                },
            ),
          ],
        ),

//        child: [RaisedButton(
//          onPressed: () {
//            Navigator.pop(context);
//          },



      ),
    );
  }
}

class MyHomePage extends StatelessWidget {
  final DatabaseHelper db;
  MyHomePage(this.db);



  @override
  Widget build(BuildContext context) {
    void _add(_ViewModel model) async{
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => SecondScreen(model)),

      );
      //Scaffold.of(context).showSnackBar(SnackBar(content: Text("$result")));
    }


    return Scaffold (
      appBar: AppBar(
        title: Text('Shopping List'),
        actions: <Widget>[
//          IconButton( //asta e butonul vechi de add
//            icon: Icon(Icons.add),
//            tooltip: 'Run again',
//            // ignore: use_of_void_result
//            //onPressed: _add,
//
//          ),
        ],
      ),

      body: StoreConnector<AppState,_ViewModel>(
          //a function which convers the shopping into the viewmodel that we want to create
          converter: (Store<AppState> store) => _ViewModel.create(store),
          builder: (BuildContext context, _ViewModel viewModel) => Column(
            children: <Widget>[
              //AddItemWidget(viewModel),
              Expanded(child: ItemListWidget(viewModel)),
              IconButton(
                icon: Icon(Icons.add),
                tooltip: 'Run again',
                // ignore: use_of_void_result
                onPressed: ()=>_add(viewModel)),
              //RemoveItemsButton(viewModel),
            ],
           ),
          ),
    );
  }
}

class _ViewModel{
  static DatabaseHelper db ;
  final List<ShoppingItem> items;
  final Function(String,String) onAddItem;
  final Function(ShoppingItem) onRemoveItem;
  final Function(ShoppingItem,String,String) onEditItem;

  _ViewModel({
    //this.db,
    this.items,
    this.onAddItem,
    this.onRemoveItem,
    this.onEditItem
});

  //set _db(DatabaseHelper _db) => this.db = _db;



  factory _ViewModel.create(Store<AppState> store){
    _onAddItem(String title,String quantity){
      //store.dispatch(AddItemAction(title, quantity));
      store.dispatch(AddItemAction(title,quantity));
      //store.dispatch(ThunkAction.saveItem);
    }

    _onRemoveItem(ShoppingItem item) {
      store.dispatch(RemoveItemAction(item));
    }

    _onEditItem(ShoppingItem item,String newTitle,String newQuantity){
      store.dispatch(EditItemAction(item,newTitle,newQuantity));
    }

    return _ViewModel(
      items:store.state.shoppingItems,
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

class _SuperAddItemState extends State<AddItemWidget> {

  @override
  Widget build(BuildContext context) {
    widget.model.onAddItem("ha","hu");
  }
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
    void _edit(_ViewModel model,ShoppingItem item) {
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => EditScreen(model,item)),

      );
    }
    return ListView(
      children: model.items
          .map(( item) => ListTile(
          title: Text(item.title+ " "+ item.quantity),
          trailing: IconButton(
              icon: Icon(Icons.edit),
              //onPressed: () => model.onEditItem(item,"textDinTextBOx")),
              onPressed: () => _edit(model,item)),
          leading: IconButton(
            icon: Icon(Icons.delete),
            onPressed: () => model.onRemoveItem(item),
          )
      ))
          .toList(),
    );
  }

}
