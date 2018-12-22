package src

import org.springframework.data.jpa.repository.JpaRepository
import src.ShoppingItem
import org.springframework.data.repository.CrudRepository

interface ItemRepository : JpaRepository<ShoppingItem, Long>