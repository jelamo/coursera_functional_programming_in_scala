package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val unionS1S2S3 = union(union(s1, s2), s3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(x) contains x") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
      assert(contains(s2, 2), "Singleton")
      assert(contains(s3, 3), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains the intersection values") {
    new TestSets {
      val unionS1S2 = union(s1, s2)
      val unionS1S2IntersectS1 = intersect(unionS1S2, s1)
      val unionS1S2IntersectS2 = intersect(unionS1S2, s2)

      assert(contains(unionS1S2IntersectS1, 1), "Intersection of 1 with union of s1 and s2 contains 1")
      assert(!contains(unionS1S2IntersectS1, 2), "Intersection of 1 with union of s1 and s2 does notcontain 2")
      assert(contains(unionS1S2IntersectS2, 2), "Intersection of 2 with union of s1 and s2 contains 2")
      assert(!contains(unionS1S2IntersectS2, 1), "Intersection of 2 with union of s1 and s2 does not contain 2")
    }
  }

  test("intersecting a set with itself contains itself") {
    new TestSets {
      val intersetS1S1 = intersect(s1, s1);
      assert(contains(intersetS1S1, 1), "Intersecting s1 with itself")
      assert(!contains(intersetS1S1, 2), "Intersecting s1 with itself does not contains 2")
      assert(!contains(intersetS1S1, 3), "Intersecting s1 with itself does not contains 3")

      val intersetS2S2 = intersect(s2, s2);
      assert(contains(intersetS2S2, 2), "Intersecting s2 with itself")
      assert(!contains(intersetS2S2, 1), "Intersecting s2 with itself does not contains 1")
      assert(!contains(intersetS2S2, 3), "Intersecting s2 with itself does not contains 2")

      val intersetS3S3 = intersect(s3, s3);
      assert(contains(intersetS3S3, 3), "Intersecting s3 with itself")
      assert(!contains(intersetS3S3, 1), "Intersecting s3 with itself does not contains 1")
      assert(!contains(intersetS3S3, 12), "Intersecting s3 with itself does not contains 2")
    }
  }

  test("dif returns the difference of two sets") {
    new TestSets {
      val difS1S1 = diff(s1, s1)
      assert(!contains(difS1S1, 1))
      assert(!contains(difS1S1, 2))
      assert(!contains(difS1S1, 3))

      val difS1S2 = diff(s1, s2)
      assert(contains(difS1S2, 1))
      assert(!contains(difS1S2, 2))
      assert(!contains(difS1S2, 3))

      val unionS1S2 = union(s1, s2)
      val unionS1S2diffS3 = diff(unionS1S2, s3)
      assert(contains(unionS1S2diffS3, 1))
      assert(contains(unionS1S2diffS3, 2))
      assert(!contains(unionS1S2diffS3, 3))
    }
  }

  test("filtering sets filters out the relevant elements") {
    new TestSets {
      val biggerThanTwo = (x:Int) => x > 2
      assert(contains(filter(unionS1S2S3, biggerThanTwo), 3), "3 is bigger than 2")
      assert(!contains(filter(unionS1S2S3, biggerThanTwo), 2), "2 is not bigger than 2")
      assert(!contains(filter(unionS1S2S3, biggerThanTwo), 1), "1 is not bigger than 2")
    }
  }

  test("forall tests a predicate in all elements of a set") {
    new TestSets {
      assert(forall(unionS1S2S3, x => x > 3) === false, "all elements of unition of 1, 2, and 3 are not > 3")
      assert(forall(unionS1S2S3, x => x <= 3) === true, "all elements of unition of 1, 2, and 3 are <= 3")
    }
  }

  test("exists tests if any element in a set satisfies a predicate") {
    new TestSets {
      assert(exists(unionS1S2S3, x => x == 3) === true, "union of 1, 2, and 3 contains an element equal to 3")
      assert(exists(unionS1S2S3, x => x == 4) === false, "union of 1, 2, and 3 does not contain an element equal to 4")
    }
  }

  test("map transformed the set by the given function") {
    new TestSets {
      val transformedSet = map(unionS1S2S3, x => x * 2)
      assert(contains(transformedSet, 2))
      assert(contains(transformedSet, 4))
      assert(contains(transformedSet, 6))
      assert(!contains(transformedSet, 1))
      assert(!contains(transformedSet, 3))
    }
  }

}
