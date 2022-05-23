package it.neckar.problem

/**
 * Commonly used status codes defined by HTTP, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10) for the complete list. Additional status codes can be added
 * by applications by creating an implementation of StatusType.
 */
enum class Status(
  /**
   * Get the associated status code.
   */
  val statusCode: Int,

  /**
   * Get the reason phrase.
   */
  val reasonPhrase: String
) {
  /**
   * @see [HTTP/1.1:
   * Semantics and Content, section 6.2.1](http://tools.ietf.org/html/rfc7231.section-6.2.1)
   */
  CONTINUE(100, "Continue"),

  /**
   * @see [HTTP/1.1:
   * Semantics and Content, section 6.2.2](http://tools.ietf.org/html/rfc7231.section-6.2.2)
   */
  SWITCHING_PROTOCOLS(101, "Switching Protocols"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc2518.section-10.1)
   */
  PROCESSING(102, "Processing"),

  /**
   * @see [A
   * proposal for supporting resumable POST/PUT HTTP requests in HTTP/1.0](http://code.google.com/p/gears/wiki/ResumableHttpRequestsProposal)
   */
  CHECKPOINT(103, "Checkpoint"),

  /**
   * 200 OK, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.1).
   */
  OK(200, "OK"),

  /**
   * 201 Created, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.2).
   */
  CREATED(201, "Created"),

  /**
   * 202 Accepted, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.3).
   */
  ACCEPTED(202, "Accepted"),

  /**
   * @see [HTTP/1.1:
   * Semantics and Content, section 6.3.4](http://tools.ietf.org/html/rfc7231.section-6.3.4)
   */
  NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

  /**
   * 204 No Content, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.5).
   */
  NO_CONTENT(204, "No Content"),

  /**
   * 205 Reset Content, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.6).
   */
  RESET_CONTENT(205, "Reset Content"),

  /**
   * 206 Reset Content, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.2.7).
   */
  PARTIAL_CONTENT(206, "Partial Content"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc4918.section-13)
   */
  MULTI_STATUS(207, "Multi-Status"),

  /**
   * @see [WebDAV Binding
   * Extensions](http://tools.ietf.org/html/rfc5842.section-7.1)
   */
  ALREADY_REPORTED(208, "Already Reported"),

  /**
   * @see [Delta
   * encoding in HTTP](http://tools.ietf.org/html/rfc3229.section-10.4.1)
   */
  IM_USED(226, "IM Used"),

  /**
   * @see [HTTP/1.1:
   * Semantics and Content, section 6.4.1](http://tools.ietf.org/html/rfc7231.section-6.4.1)
   */
  MULTIPLE_CHOICES(300, "Multiple Choices"),

  /**
   * 301 Moved Permanently, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.2).
   */
  MOVED_PERMANENTLY(301, "Moved Permanently"),

  /**
   * 302 Found, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.3).
   */
  FOUND(302, "Found"),

  /**
   * 303 See Other, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.4).
   */
  SEE_OTHER(303, "See Other"),

  /**
   * 304 Not Modified, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.5).
   */
  NOT_MODIFIED(304, "Not Modified"),

  /**
   * 305 Use Proxy, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.6).
   */
  USE_PROXY(305, "Use Proxy"),

  /**
   * 307 Temporary Redirect, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.8).
   */
  TEMPORARY_REDIRECT(307, "Temporary Redirect"),

  /**
   * @see [RFC 7238](http://tools.ietf.org/html/rfc7238)
   */
  PERMANENT_REDIRECT(308, "Permanent Redirect"),

  /**
   * 400 Bad Request, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1).
   */
  BAD_REQUEST(400, "Bad Request"),

  /**
   * 401 Unauthorized, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.2).
   */
  UNAUTHORIZED(401, "Unauthorized"),

  /**
   * 402 Payment Required, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.3).
   */
  PAYMENT_REQUIRED(402, "Payment Required"),

  /**
   * 403 Forbidden, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.4).
   */
  FORBIDDEN(403, "Forbidden"),

  /**
   * 404 Not Found, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5).
   */
  NOT_FOUND(404, "Not Found"),

  /**
   * 405 Method Not Allowed, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.6).
   */
  METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

  /**
   * 406 Not Acceptable, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.7).
   */
  NOT_ACCEPTABLE(406, "Not Acceptable"),

  /**
   * 407 Proxy Authentication Required, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.8).
   */
  PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

  /**
   * 408 Request Timeout, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.9).
   */
  REQUEST_TIMEOUT(408, "Request Timeout"),

  /**
   * 409 Conflict, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.10).
   */
  CONFLICT(409, "Conflict"),

  /**
   * 410 Gone, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.11).
   */
  GONE(410, "Gone"),

  /**
   * 411 Length Required, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.12).
   */
  LENGTH_REQUIRED(411, "Length Required"),

  /**
   * 412 Precondition Failed, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.13).
   */
  PRECONDITION_FAILED(412, "Precondition Failed"),

  /**
   * 413 Request Entity Too Large, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.14).
   */
  REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),

  /**
   * 414 Request-URI Too Long, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.15).
   */
  REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),

  /**
   * 415 Unsupported Media Type, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.16).
   */
  UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

  /**
   * 416 Requested Range Not Satisfiable, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.17).
   */
  REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),

  /**
   * 417 Expectation Failed, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.18).
   */
  EXPECTATION_FAILED(417, "Expectation Failed"),

  /**
   * @see [HTCPCP/1.0](http://tools.ietf.org/html/rfc2324.section-2.3.2)
   */
  I_AM_A_TEAPOT(418, "I'm a teapot"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc4918.section-11.2)
   */
  UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc4918.section-11.3)
   */
  LOCKED(423, "Locked"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc4918.section-11.4)
   */
  FAILED_DEPENDENCY(424, "Failed Dependency"),

  /**
   * @see [Upgrading to TLS
   * Within HTTP/1.1](http://tools.ietf.org/html/rfc2817.section-6)
   */
  UPGRADE_REQUIRED(426, "Upgrade Required"),

  /**
   * @see [Additional HTTP
   * Status Codes](http://tools.ietf.org/html/rfc6585.section-3)
   */
  PRECONDITION_REQUIRED(428, "Precondition Required"),

  /**
   * @see [Additional HTTP
   * Status Codes](http://tools.ietf.org/html/rfc6585.section-4)
   */
  TOO_MANY_REQUESTS(429, "Too Many Requests"),

  /**
   * @see [Additional HTTP
   * Status Codes](http://tools.ietf.org/html/rfc6585.section-5)
   */
  REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

  /**
   * @see [Unavailable For
   * Legal Reasons](https://tools.ietf.org/html/rfc7725.section-3)
   */
  UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

  /**
   * 500 Internal Server Error, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.1).
   */
  INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

  /**
   * 501 Not Implemented, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.2).
   */
  NOT_IMPLEMENTED(501, "Not Implemented"),

  /**
   * 502 Bad Gateway, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.3).
   */
  BAD_GATEWAY(502, "Bad Gateway"),

  /**
   * 503 Service Unavailable, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.4).
   */
  SERVICE_UNAVAILABLE(503, "Service Unavailable"),

  /**
   * 504 Gateway Timeout, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.5).
   */
  GATEWAY_TIMEOUT(504, "Gateway Timeout"),

  /**
   * 505 HTTP Version Not Supported, see [HTTP/1.1
 * documentation](http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.5.6).
   */
  HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),

  /**
   * @see [Transparent
   * Content Negotiation](http://tools.ietf.org/html/rfc2295.section-8.1)
   */
  VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),

  /**
   * @see [WebDAV](http://tools.ietf.org/html/rfc4918.section-11.5)
   */
  INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

  /**
   * @see [WebDAV Binding
   * Extensions](http://tools.ietf.org/html/rfc5842.section-7.2)
   */
  LOOP_DETECTED(508, "Loop Detected"),

  /**
   * `509 Bandwidth Limit Exceeded`
   */
  BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),

  /**
   * @see [HTTP Extension
   * Framework](http://tools.ietf.org/html/rfc2774.section-7)
   */
  NOT_EXTENDED(510, "Not Extended"),

  /**
   * @see [Additional HTTP
   * Status Codes](http://tools.ietf.org/html/rfc6585.section-6)
   */
  NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

  /**
   * Get the Status String representation.
   *
   * @return the status code and reason.
   */
  override fun toString(): String {
    return "$statusCode $reasonPhrase"
  }

  companion object {
    private val statuses: Map<Int, Status> = values().associateBy { it.statusCode }

    /**
     * Creates a Status instance from the given code.
     *
     * @param code the HTTP code as a number
     * @return the correct enum value for this status code.
     *
     * @throws IllegalArgumentException if the given code does not correspond to a known HTTP status.
     */
    fun valueOf(code: Int): Status {
      return statuses[code] ?: throw IllegalArgumentException("There is no known status for this code ($code).")
    }
  }
}
