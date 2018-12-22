package src

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class Controller(private val repository: ItemRepository) {

    @GetMapping("/items")
    fun getItems(): List<ShoppingItem> =
            repository.findAll()


    @PostMapping("/items")
    fun createNewShoppingItem(@Valid @RequestBody item: ShoppingItem): ShoppingItem =
            repository.save(item)


    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable(value = "id") todoId: Long): ResponseEntity<ShoppingItem> {
        return repository.findById(todoId).map { item ->
            ResponseEntity.ok(item)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/items/{id}")
    fun updateItemById(@PathVariable(value = "id") itemId: Long,
                       @Valid @RequestBody item: ShoppingItem): ResponseEntity<ShoppingItem> {

        return repository.findById(itemId).map { existingItem ->
            val updatedItem: ShoppingItem = existingItem
                    .copy(title = item.title)
            ResponseEntity.ok().body(repository.save(updatedItem))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/items/{id}")
    fun deleteItemById(@PathVariable(value = "id") todoId: Long): ResponseEntity<Void> {

        return repository.findById(todoId).map { item  ->
            repository.delete(item)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}