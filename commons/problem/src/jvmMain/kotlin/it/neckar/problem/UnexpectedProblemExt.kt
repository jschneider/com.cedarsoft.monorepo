package it.neckar.problem

fun Throwable.toUnexpectedProblem(): UnexpectedProblem {
  return UnexpectedProblem(
    this.message,
    this::class.qualifiedName ?: this::class.simpleName ?: this.toString(),
    null,
    this.stackTraceToString()
  )
}
