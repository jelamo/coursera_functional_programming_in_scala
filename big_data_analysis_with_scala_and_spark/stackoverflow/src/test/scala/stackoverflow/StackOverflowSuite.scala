package stackoverflow

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {


  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }


  test("'groupedPosting' should group answers by question id") {
    import StackOverflow._

    val question1 = Posting(1, 1, Some(2), None, 33, Some("a, b, c"))
    val question2 = Posting(1, 4, None, None, 54, Some("a, b, c"))
    val answer1 = Posting(2, 2, None, Some(1), 14, Some("a, b, c"))
    val answer2 = Posting(2, 3, None, Some(1), 24, Some("a, b, c"))
    val answer3 = Posting(2, 5, None, Some(4), 18, Some("a, b, c"))

    val rdd = sc.parallelize(Seq(question1, question2, answer1, answer2, answer3))

    val groupedPostings = StackOverflow.groupedPostings(rdd).collect().sortBy(_._1)

    assert(groupedPostings.length == 2)
    assert(groupedPostings(0)._1 == 1)
    assert(groupedPostings(1)._1 == 4)
    assert(groupedPostings(0)._2.toList == List((question1, answer1), (question1, answer2)))
    assert(groupedPostings(1)._2.toList == List((question2, answer3)))
  }


  test("'scoredPosting' should return pairs of question and highest scored from answers") {
    import StackOverflow._

    val question1 = Posting(1, 1, Some(2), None, 33, Some("a, b, c"))
    val question2 = Posting(1, 4, None, None, 54, Some("a, b, c"))
    val answer1 = Posting(2, 2, None, Some(1), 14, Some("a, b, c"))
    val answer2 = Posting(2, 3, None, Some(1), 24, Some("a, b, c"))
    val answer3 = Posting(2, 5, None, Some(4), 18, Some("a, b, c"))

    val rdd = sc.parallelize(Seq(question1, question2, answer1, answer2, answer3))

    val groupedPostings = StackOverflow.groupedPostings(rdd)
    val scoredPostings = StackOverflow.scoredPostings(groupedPostings).collect().sortBy(_._1.id)

    assert(scoredPostings.length == 2)
    assert(scoredPostings(0)._1.id == 1)
    assert(scoredPostings(1)._1.id == 4)
    assert(scoredPostings(0)._2 == 24)
    assert(scoredPostings(1)._2 == 18)
  }

}
