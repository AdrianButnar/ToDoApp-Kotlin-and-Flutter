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
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:dio/dio.dart';
import 'dart:io';
import 'package:fluttertoast/fluttertoast.dart';


Dio dio = new Dio();
String basepath = 'http://10.0.2.2:8080/api';
var db = new DatabaseHelper();
List<ShoppingItem> syncList = new List();

Future syncLocalToDB() async {
  for (ShoppingItem item in syncList){
    await dio.post(basepath+"/items",data: item.toMap());//!!VEZI CA NU IA ID-ul de aici, pune altul in functie de primary key

  }
  syncList.clear();
}

Future<bool> accessToInternet() async {
  try {
    final result = await InternetAddress.lookup('www.google.com');
    if (result.isNotEmpty && result[0].rawAddress.isNotEmpty) {
      return true;
    }
    return false;
  }
  catch(Exception){
    return false;
  }
}

Future<List<ShoppingItem>> fetchItems() async {
  final response =
  await http.get('http://10.0.2.2:8080/api/items');

  return parseItems(response.body);
}

List<ShoppingItem> parseItems(String responseBody) {
  final parsed = json.decode(responseBody).cast<Map<String, dynamic>>();

  return parsed.map<ShoppingItem>((json) => ShoppingItem.fromMap(json)).toList();
}
Future main() async{
  print(await accessToInternet());
  //await db.saveNote(new ShoppingItem("Flutter Tutorials"));
  db.initDb();
  List initialShoppingList = await db.getAllItems();
  //db.saveItem(ShoppingItem(id: -2, title: "HARDCODED", quantity: "ITEM"));
  List<ShoppingItem> newList = new List<ShoppingItem>();
  initialShoppingList.forEach((item)=>(newList.add(ShoppingItem.fromMap(item))));

  //ceva pentru server part

//
//   final http.Response response = await http.get('https://jsonplaceholder.typicode.com/posts/1');
//   if(response.statusCode == 200){
//     return json.decode(response.body);
//   }
  if(await accessToInternet()) {
    newList = await fetchItems();

    //sync remote db to localdb
    initialShoppingList.forEach((item)=>(db.deleteItem(ShoppingItem.fromMap(item).id)));
    newList.forEach((item)=>(db.saveItem(item)));
  }
  //var response = await dio.post(basepath+"/items",data: new ShoppingItem(id: -2, title: 'buna', quantity: 'cf').toMap());
  //var response = await dio.delete('http://10.0.2.2:8080/api/items/8',data:8);
  //FormData formData = new FormData.from({"id":7,"ShoppingItem":new ShoppingItem(id: 7, title: 'update', quantity: 'works').toMap()});
  //var response = await dio.put('http://10.0.2.2:8080/api/items/7',data: formData);
  //var response = await dio.put('http://10.0.2.2:8080/api/items/7',data: new ShoppingItem(id: 7, title: 'update', quantity: 'works').toSuperMap());

  runApp(MyApp(db,newList));
}

class MyApp extends StatelessWidget {
  final DatabaseHelper db;
  final List<ShoppingItem> newList;
  MyApp(this.db, this.newList);



  @override
  Widget build(BuildContext context) {
    final Store<AppState> store = Store<AppState>(
      appStateReducer,
      initialState: AppState.initialState(db,newList),
      middleware: [thunkMiddleware]
    );
    return StoreProvider(
        store : store,
        child: MaterialApp(
          title: 'Shopping List',
          home: MyHomePage(db),
        )
    );
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
                        hintText: item.title.toString(),
                      ),
                    ),
                  ),
                  TextField(
                    controller: myController2,
                    style: TextStyle(
                      color: Colors.black,
                    ),
                    decoration: InputDecoration(
                        hintText: item.quantity.toString()
                    ),
                  ),
                ],
              ),
            ),
            RaisedButton(
              child: const Text("Edit"),
              onPressed: () {
                //AddItemWidget(myController1.text,myController2.text);
                model.onEditItem(model.db,item,myController1.text,myController2.text);
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
                model.onAddItem(model.db,myController1.text,myController2.text);
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
            Expanded(child: ItemListWidget(viewModel._db(db))),
            IconButton(
                icon: Icon(Icons.add),
                tooltip: 'Run again',
                // ignore: use_of_void_result
                onPressed: ()=>_add(viewModel._db(db))),
            //RemoveItemsButton(viewModel),
          ],
        ),
      ),
    );
  }
}

class EditItemAction{
  final ShoppingItem item;
  final String newTitle;
  final String newQuantity;
  EditItemAction(this.item,this.newTitle,this.newQuantity);
}

class RemoveItemAction{
  final ShoppingItem item;
  RemoveItemAction(this.item);
}

class AddItemAction<AppState> {
  int id;
  //List<ShoppingItem> items;
  String title;
  final String quantity;
  DatabaseHelper db;



//  AddItemAction(this.title,this.quantity) {
//    _id++;
//  }

  AddItemAction(this.id,this.title, this.quantity, DatabaseHelper db) {

  }
}

class _ViewModel{
  DatabaseHelper db ;
  final List<ShoppingItem> items;
  final Function(DatabaseHelper,String,String) onAddItem;
  final Function(DatabaseHelper,ShoppingItem) onRemoveItem;
  final Function(DatabaseHelper,ShoppingItem,String,String) onEditItem;

  _ViewModel({
    this.db,
    this.items,
    this.onAddItem,
    this.onRemoveItem,
    this.onEditItem
  });

  _ViewModel _db(DatabaseHelper _db){this.db = _db; return this;}



  factory _ViewModel.create(Store<AppState> store){
    _onAddItem(DatabaseHelper db,String title,String quantity){
      //store.dispatch(AddItemAction(title, quantity));
      ThunkAction<AppState> addThunk = (Store<AppState> store) async {
        int maxId;
        if(await db.getCount()==null){
          maxId=1;
        }
        else{
          maxId= await db.getCount();
        }
        while (await db.getItem(maxId)!=null){
          maxId++;
        }
        await db.saveItem(ShoppingItem(id: maxId, title: title, quantity: quantity));
        if(await accessToInternet()){
          syncLocalToDB();
          await dio.post(basepath+"/items",data: new ShoppingItem(id: -2, title: title, quantity: quantity).toMap());//!!VEZI CA NU IA ID-ul de aici, pune altul in functie de primary key
        }
        else{
          syncList.add(ShoppingItem(id: maxId, title: title, quantity: quantity));
        }
        List elems = await db.getAllItems();
        List<ShoppingItem> newList = new List<ShoppingItem>();
        elems.forEach((item) => (newList.add(ShoppingItem.fromMap(item))));
        //return newList;
        store.dispatch(AddItemAction(maxId,title,quantity,db));

      };
      store.dispatch(addThunk);
      //store.dispatch(ThunkAction.saveItem);
    }



    _onRemoveItem(DatabaseHelper db,ShoppingItem item) {
      ThunkAction<AppState> removeThunk = (Store<AppState> store) async {
        if(await accessToInternet()) {
          syncLocalToDB();
          await db.deleteItem(item.id);
          await dio.delete(basepath + '/items/' + item.id.toString(), data: item.id);
          store.dispatch(RemoveItemAction(item));

        }
        else{
          Fluttertoast.showToast(
              msg: "Delete cannot be performed offline",
              toastLength: Toast.LENGTH_SHORT,
              gravity: ToastGravity.CENTER,
              timeInSecForIos: 1
          );
        }
        //return newList;
      };
      store.dispatch(removeThunk);

    }

    _onEditItem(DatabaseHelper db,ShoppingItem item,String newTitle,String newQuantity){
      ThunkAction<AppState> editThunk = (Store<AppState> store) async {
          syncLocalToDB();
          await db.deleteItem(item.id);
          await db.saveItem(ShoppingItem(id: item.id, title: newTitle, quantity: newQuantity));
          await dio.put(basepath + '/items/' + item.id.toString(),data: new ShoppingItem(id: item.id, title: newTitle, quantity: newQuantity).toSuperMap());
          List elems = await db.getAllItems();
          List<ShoppingItem> newList = new List<ShoppingItem>();
          elems.forEach((item) => (newList.add(ShoppingItem.fromMap(item))));
          //return newList;
          store.dispatch(EditItemAction(item, newTitle, newQuantity));
      };
      store.dispatch(editThunk);
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

//class _SuperAddItemState extends State<AddItemWidget> {
//
//  @override
//  Widget build(BuildContext context) {
//    widget.model.onAddItem("ha","hu");
//  }
//}

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
        //widget.model.onAddItem(title,"");
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
    Future _edit(_ViewModel model,ShoppingItem item) async {
      if(await accessToInternet()) {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => EditScreen(model, item)),

        );
      }
      else{
        Fluttertoast.showToast(
            msg: "Edit cannot be performed offline",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.CENTER,
            timeInSecForIos: 1
        );
      }
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
            onPressed: () => model.onRemoveItem(model.db,item),
          )
      ))
          .toList(),
    );
  }
}