package bitmage;
import bitmage.Enums.FileStatus;
import bitmage.Utils.Validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<Image> images = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        String path;

        System.out.println("\nSet console screen buffer width to max before generating anything - console will not display images properly, if source image is wider than screen buffer\n");
        System.out.println("""
            Manual:\s
            \s
                "gen (options) (filepath)" - generate from file\s
                \s
                Example:\s
                    gen -m -i C:\\Users\\John_Doe\\Image.png
                    \s
                    Options:\s
                        m - render image with 16-char map (default - 8)\s
                        i - invert image colors\s
                    \s
                    Default options will be applied if not specified:\s
                        8-char map\s
                        Normal image colors\s
                   \s
                "exit" - close the program
        """);

        mainLoop:
        while (true)
        {
            do
            {
                System.out.print(">>> "); path = scanner.next();

                if (path.equalsIgnoreCase("exit"))
                {
                    break mainLoop;
                }

                if (Validation.validatePath(path).getStatus() != FileStatus.FILE_OK)
                {
                    System.out.println("\n" + Validation.validatePath(path).getMessage());
                }

            } while (Validation.validatePath(path).getStatus() != FileStatus.FILE_OK);

            try
            {
                System.out.println("\n\n\n");

                images.add(new Image(path));

                images.getLast().print();

                System.out.println("\n\n == Done == \n");
            }
            catch (InputMismatchException | IOException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
