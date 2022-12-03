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

  operator fun plus(additionalTag: Tag): Tags {
    return Tags(buildSet {
      addAll(tags)
      add(additionalTag)
    })
  }

  fun format(): String {
    return tags.joinToString { it.id }
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

fun Tags?.contains(tag: Tag): Boolean {
  return this != null && this.contains(tag)
}

/**
 * Returns true if this is null or none of the provided tags are within this.
 *
 * Attention: This method does not use varargs to avoid unnecessary instantiations of objects
 */
fun Tags?.containsNone(tag0: Tag, tag1: Tag, tag2: Tag, tag3: Tag, tag4: Tag): Boolean {
  if (this == null) {
    return true
  }

  return this.contains(tag0).not()
    && this.contains(tag1).not()
    && this.contains(tag2).not()
    && this.contains(tag3).not()
    && this.contains(tag4).not()
}

fun Tag?.toTags(): Tags? {
  if (this == null) {
    return null
  }

  return toTags()
}

fun Tag?.toTagsOrEmpty(): Tags {
  if (this == null) {
    return Tags.empty
  }

  return toTags()
}

fun Tag.toTags(): Tags {
  return Tags(this)
}
