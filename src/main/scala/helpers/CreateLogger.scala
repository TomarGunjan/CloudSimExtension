package helpers

import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Success, Try}

/**
 * This is a utility class for creating logger
 */
object CreateLogger {


  def apply[T](class4Logger: Class[T]):Logger = {
    val LOGBACKXML = "logback.xml"
    val logger = LoggerFactory.getLogger(class4Logger)
    Try(getClass.getClassLoader.getResourceAsStream(LOGBACKXML)) match {
      case Failure(exception) => logger.error(s"Failed to locate $LOGBACKXML for reason $exception")
      case Success(inStream) => inStream.close()
    }
    logger
  }

}
