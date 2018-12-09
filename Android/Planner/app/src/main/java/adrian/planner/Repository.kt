package adrian.planner

import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Movie, Long>
