import java.io.File

import Program.{Index, NotDirectory, ReadFileError, countText}
import org.scalatest.FunSuite

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.io.StdIn.readLine

class ProgramTest extends FunSuite {
  test("Program.index") {
    val file = new File("/home/leonid/Programs/TextFiles/")
    val lst = file.listFiles().filter(f => f.getAbsolutePath.endsWith("txt")).toList
    assert(Program.index(file) === Index(lst))
  }
  test("Program.findWords") {
    val file = new File("/home/leonid/Programs/TextFiles/1")
    val lst = file.listFiles().filter(f => f.getAbsolutePath.endsWith("txt")).toList
    val searchString = "test service"
    val resultList = ListBuffer.empty[String]
    var ranks = Map.empty[String, Int]
    val files = Index(lst).filesLst
    assert(Program.findWords(searchString, new File("/home/leonid/Programs/TextFiles/1/tr823.txt")) === """50 tr823.txt""")
  }

  test("Program.countText") {
    val searchString = "test service"
    val strArray = searchString.trim.split("\\s+")
    var wordsCount = strArray.length
    var wordsInText = 0
    try {
      val source = Source.fromFile(new File("/home/leonid/Programs/TextFiles/1/tr823.txt"), "ISO-8859-1")
      val lines = source.getLines()
      val lower = lines.map(_.toLowerCase).map(_.replace(",", ""))
      val linesList = lower.flatMap(x => x.split("\\s+").map(x => x.trim))

      assert(Program.countText(strArray(0), linesList) === """true""")
      assert(Program.countText(strArray(1), linesList) === """false""")

      source.close()
    }
    catch {
      case ex: Exception => println(s"File reading error. Description: $ex")
    }
  }


}


