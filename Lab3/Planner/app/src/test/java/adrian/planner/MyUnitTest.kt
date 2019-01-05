package adrian.planner

import com.google.firebase.database.FirebaseDatabase
import org.junit.Test

import org.junit.Assert.*

class MyUnitTest() {
    @Test fun CRUDTest(){
        val ref = FirebaseDatabase.getInstance().getReference("items");
    }
}