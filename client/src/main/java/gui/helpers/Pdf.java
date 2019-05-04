package gui.helpers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * Class to open a file.
 */
public class Pdf {

    /**
     * Method which opens a file, operating system will
     * automatically use the default application for it.
     * @param fileName File the program wants to open.
     */
    public static void openFile(String fileName) {
        String defaultPath = "doc\\miscellaneous\\".replaceAll(
                "[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
        String path = defaultPath + fileName;
        File file = new File(path);
        try {
            if (Desktop.isDesktopSupported()) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (NullPointerException e) {
            System.out.println("PDF not found!");
        }
    }
}
