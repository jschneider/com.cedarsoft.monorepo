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
