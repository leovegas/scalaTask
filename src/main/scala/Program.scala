import java.io.File
import java.nio.charset.Charset

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try
import scala.util.control.Breaks.break

object Program {
  import scala.io.StdIn.readLine

  case class Index(filesLst:List[File])

  sealed trait ReadFileError

  case object MissingPathArg extends ReadFileError
  case class NotDirectory(error: String) extends ReadFileError
  case class FileNotFound(t: Throwable) extends ReadFileError

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }


  def findWords(string: String, file: File):String = {
    val strArray = string.trim.split("\\s+")
    var wordsCount = strArray.length
    var wordsInText = 0
    try {
      val source = Source.fromFile(file,"ISO-8859-1")
      val lines = source.getLines()
      val lower = lines.map(_.toLowerCase).map(_.replace(",",""))
      val linesList = lower.flatMap(x=>x.split("\\s+").map(x=>x.trim))
      for (str <- strArray) {
        if (countText(str, linesList)) {
          wordsInText += 1
        }
      }
      source.close()
    }
    catch {
      case ex: Exception => println(s"File reading error. Description: $ex")
    }
    s"${(100*wordsInText)/wordsCount} ${file.getName}"
  }

  def countText(string: String, linesList: Iterator[String]): Boolean = {
    linesList.exists(p => p.equalsIgnoreCase(string))
  }

  def index(file: File): Index = {
    val lst = file.listFiles().filter(f => f.getAbsolutePath.endsWith("txt")).toList
    println(s"${lst.size} files read in directory $file")
    Index(lst)
    }

  def iterate(indexedFiles: Index): Unit = {
    print(s"search> ")
    val searchString = readLine()
    val resultList = ListBuffer.empty[String]
    var ranks = Map.empty[String, Int]
    val files = indexedFiles.filesLst
    files.foreach(f => if (f.isFile) resultList += findWords(searchString, f))
    resultList.foreach(res => {
      val resArray = res.split("\\s+")
      ranks += (resArray(1) -> resArray(0).toInt)
    })
    ranks.toSeq.sortBy(_._2).reverse.take(10).foreach(out => println(s"${out._1} ${out._2} %"))
    iterate(indexedFiles)
  }
}
