# Getting JavaFX to work
OpenJDK 11 doesn't include JavaFX by default anymore, so you have to add it manually if you want to add it to the project.

## Adding the JavaFX development library
This library is needed for developing the JavaFX application, and contains all the (non-compiled) Java libraries.  
The client maven project has the JavaFX development library  added to its dependencies, so you don't have to do anything here.
This means that writing code will work fine out of the box.

## Adding the JavaFX runtime library
This library is needed to actually run your JavaFX application and get a GUI window on your screen.  
As this library is system specific, you have to download and add it to the project yourself.  
The steps for this are:
1. Download the appropriate JavaFX SDK from [here](https://gluonhq.com/products/javafx/)
    * Don't place the files inside a folder which contains spaces (e.g. _Program Files_) because this may cause some errors while running the application 
2. Add the library directory to your systems variables
    * **macOS/Linux**  
    Run `$ export PATH_TO_FX=path/to/javafx-sdk-11.0.2/lib` in terminal
    * **Windows**  
    Run `$ set PATH_TO_FX="path\to\javafx-sdk-11.0.2\lib"` in command prompt  
3. Point to the lib in IntelliJ
    * Go to 'Run > Edit Configurations...'
    * In 'VM options' add `--module-path path/to/javafx-sdk-11.0.2/lib --add-modules=javafx.controls`
4. Done! If everything went well you can now run your JavaFX class in IntelliJ.