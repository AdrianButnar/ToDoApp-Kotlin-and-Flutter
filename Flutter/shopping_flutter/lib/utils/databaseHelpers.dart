import 'dart:async';

import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import 'package:shopping_flutter/model/model.dart';

class DatabaseHelper {
  static final DatabaseHelper _instance = new DatabaseHelper.internal();

  factory DatabaseHelper() => _instance;

  final String tableItem = 'itemTable';
  final String columnId = 'id';
  final String columnTitle = 'title';
  final String columnQuantity = 'quantity';

  static Database _db;

  DatabaseHelper.internal();

  Future<Database> get db async {
    if (_db != null) {
      return _db;
    }
    _db = await initDb();

    return _db;
  }

  initDb() async {
    String databasesPath = await getDatabasesPath();
    String path = join(databasesPath, 'items.db');

    //await deleteDatabase(path); // just for testing

    var db = await openDatabase(path, version: 1, onCreate: _onCreate);
    return db;
  }

  void _onCreate(Database db, int newVersion) async {
    await db.execute(
        'CREATE TABLE $tableItem($columnId INTEGER PRIMARY KEY, $columnTitle TEXT, $columnQuantity TEXT)');
  }

  Future<int> saveItem(ShoppingItem item) async {
    var dbClient = await db;
    var result = await dbClient.insert(tableItem, item.toMap());
//    var result = await dbClient.rawInsert(
//        'INSERT INTO $tableNote ($columnTitle, $columnDescription) VALUES (\'${note.title}\', \'${note.description}\')');

    return result;
  }

  Future<List> getAllItems() async {
    var dbClient = await db;
    var result = await dbClient.query(tableItem, columns: [columnId, columnTitle, columnQuantity]);
//    var result = await dbClient.rawQuery('SELECT * FROM $tableNote');

    return result.toList();
  }

  Future<int> getCount() async {
    var dbClient = await db;
    return Sqflite.firstIntValue(await dbClient.rawQuery('SELECT COUNT(*) FROM $tableItem'));
  }

  Future<ShoppingItem> getItem(int id) async {
    var dbClient = await db;
    List<Map> result = await dbClient.query(tableItem,
        columns: [columnId, columnTitle, columnQuantity],
        where: '$columnId = ?',
        whereArgs: [id]);
//    var result = await dbClient.rawQuery('SELECT * FROM $tableNote WHERE $columnId = $id');

    if (result.length > 0) {
      return new ShoppingItem.fromMap(result.first);
    }

    return null;
  }

  Future<int> deleteItem(int id) async {
    var dbClient = await db;
    return await dbClient.delete(tableItem, where: '$columnId = ?', whereArgs: [id]);
//    return await dbClient.rawDelete('DELETE FROM $tableNote WHERE $columnId = $id');
  }

  Future<int> updateItem(ShoppingItem item) async {
    var dbClient = await db;
    return await dbClient.update(tableItem, item.toMap(), where: "$columnId = ?", whereArgs: [item.id]);
//    return await dbClient.rawUpdate(
//        'UPDATE $tableNote SET $columnTitle = \'${note.title}\', $columnDescription = \'${note.description}\' WHERE $columnId = ${note.id}');
  }

  Future close() async {
    var dbClient = await db;
    return dbClient.close();
  }
}
