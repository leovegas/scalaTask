implicit class AddLeo(val str:String) {
  def addleo():String = {
    str + "Leo";
  }
}


val test = "Test";
println(test.addleo())