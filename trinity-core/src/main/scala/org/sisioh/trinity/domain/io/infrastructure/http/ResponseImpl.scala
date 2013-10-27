package org.sisioh.trinity.domain.io.infrastructure.http

import com.twitter.finagle.http.{Response => FinagleResponse}
import org.jboss.netty.handler.codec.http.{HttpResponse => NettyResponse}
import org.sisioh.trinity.domain.io.buffer.ChannelBuffer
import org.sisioh.trinity.domain.io.http.ResponseStatus.{toNetty, toTrinity}
import org.sisioh.trinity.domain.io.http.{Cookie, ProtocolVersion, Response, ResponseStatus}


/**
 * Netty Requestのラッパー。
 *
 * @param toUnderlyingAsFinagle
 */
private[trinity]
case class ResponseImpl(override val toUnderlyingAsFinagle: FinagleResponse)
  extends AbstractMessage(toUnderlyingAsFinagle) with Response {

  def this(status: ResponseStatus.Value,
           headers: Seq[(String, Any)] = Seq.empty,
           cookies: Seq[Cookie] = Seq.empty,
           content: ChannelBuffer = ChannelBuffer.empty,
           protocolVersion: ProtocolVersion.Value = ProtocolVersion.Http11) = {
    this(FinagleResponse(protocolVersion, status))
    setHeaders(headers)
    setCookies(cookies)
    setContent(content)
  }

  def isRequest: Boolean = false

  protected def createInstance(message: AbstractMessage): this.type =
    new ResponseImpl(message.toUnderlyingAsFinagle.asInstanceOf[FinagleResponse]).asInstanceOf[this.type]

  protected def mutateAsResponse(f: (NettyResponse) => Unit): this.type = {
    val cloned = if (!isMutable) {
      createInstance(this)
    } else {
      this
    }
    f(cloned.toUnderlyingAsFinagle)
    cloned.asInstanceOf[this.type]
  }

  def responseStatus: ResponseStatus.Value = toUnderlyingAsFinagle.getStatus()

  def withResponseStatus(status: ResponseStatus.Value): this.type = mutateAsResponse {
    _.setStatus(status)
  }

}