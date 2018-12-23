package src

import javax.persistence.*

@Entity
@Table(name = "shoppingItems")
data class ShoppingItem(
        //@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        val id: Long,
        val title: String,
        val quantity: String)