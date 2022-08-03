package it.neckar.commons.tags

import kotlinx.serialization.Serializable

/**
 * Contains 0..n tags
 */
@Serializable(with = TagsSerializer::class)
data class Tags(
  val tags: Set<Tag>,
) {

  fun contains(tag: Tag): Boolean {
    return this.tags.contains(tag)
  }

  fun isEmpty(): Boolean {
    return this.tags.isEmpty()
  }

  companion object {
    operator fun invoke(tag: Tag): Tags {
      return Tags(setOf(tag))
    }

    operator fun invoke(tag0: Tag, tag1: Tag): Tags {
      return Tags(setOf(tag0, tag1))
    }

    operator fun invoke(tag0: Tag, tag1: Tag, tag2: Tag): Tags {
      return Tags(setOf(tag0, tag1, tag2))
    }

    val empty: Tags = Tags(setOf())
  }
}
