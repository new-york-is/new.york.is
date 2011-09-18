import com.twitter.util.Future
import com.newyorkis.util.RichFuture

package object newyorkis {
  implicit def RichFuture[A](future: Future[A]): RichFuture[A] = new RichFuture(future)
}