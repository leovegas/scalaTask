  object Main extends App {
    Program
      .readFile(args)
      .fold(
        println,
        file => Program.iterate(Program.index(file))
      )

  }

