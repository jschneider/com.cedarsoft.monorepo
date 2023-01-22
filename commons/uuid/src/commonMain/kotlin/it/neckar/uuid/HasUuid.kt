package it.neckar.uuid

import com.benasher44.uuid.Uuid

/**
 * Describes an element that has a UUID.
 *
 * **Attention**:
 * * All implementations *must* implement equals/hashCode. And these methods *must* only use [uuid].
 * * All other properties must *not* be used to compare the objects.
 *
 * It is suggested to extend [AbstractHasUuid] which provides correct implementations for equals/hashCode.
 *
 * A UUID identifies a "logical" element. E.g. it describes the roof facing west on a given building.
 * It does *not* identify a "version" of a description object.
 *
 * Therefore, there may exist several *different* objects with the same UUID that describe the same object.
 * When for example the label of the roof is updated, the *same* UUID is used.
 *
 */
interface HasUuid {
  /**
   * The UUID for the element.
   */
  val uuid: Uuid
}


/**
 * Returns a new list with the replaced module
 */
fun <T : HasUuid> List<T>.withReplaced(updatedElement: T): List<T> {
  return withReplaced(updatedElement.uuid, updatedElement)
}

fun <T : HasUuid> List<T>.withReplaced(updatedElements: List<T>): List<T> {
  return this.toMutableList()
    .also { mutableList ->
      updatedElements.forEach { updatedElement ->
        //find the index of the existing element
        val index = indexOfFirst {
          it.uuid == updatedElement.uuid
        }

        if (index < 0) {
          throw IllegalArgumentException("No index found for ${updatedElement.uuid}")
        }

        mutableList[index] = updatedElement
      }
    }
}

fun <T : HasUuid> List<T>.withReplaced(uuid: Uuid, updatedElement: T): List<T> {
  return this.toMutableList()
    .also { mutableList ->
      //find the index of the existing element
      val index = indexOfFirst {
        it.uuid == uuid
      }

      if (index < 0) {
        return withAdded(updatedElement)
      }

      mutableList[index] = updatedElement
    }
}

fun <T : HasUuid> List<T>.withAdded(addedElement: T): List<T> {
  return this.toMutableList() + addedElement
}

/**
 * Returns a new list without the element(s) described by the given UUID
 */
fun <T : HasUuid> List<T>.withRemoved(removedElement: T): List<T> {
  return filter {
    it.uuid != removedElement.uuid
  }
}
