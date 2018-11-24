package shoppinglistrealm.adi.shoppinlistrealm

import android.support.annotation.IntegerRes
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

//@RealmClass
open class ShoppingItem(@PrimaryKey var id: Int = 0, var title: String="", var quantity: String="" ) : RealmObject(){}
