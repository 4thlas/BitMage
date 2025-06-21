package bitmage;
import bitmage.Enums.FileStatus;
import bitmage.Enums.Option;
import bitmage.Utils.Command;
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

        String path = "";


        System.out.println("\nEnter \"help\" for manual");
        String manualText = """
            Set console screen buffer width to max before generating anything - console will not display images properly, if source image is wider than screen buffer\s
            \s
            "gen (options) (filepath)" - generate from file\s
            \s
            Example:\s
                gen -m -i C:\\Users\\John_Doe\\Image.png\s
                \s
                Wrap filepath in parentheses if it contains whitespaces
                \s
            Options:\s
                m - render image with 16-char map\s
                i - invert image colors\s
            \s
            Default options will be applied if not specified:\s
                8-char map\s
                Normal image colors\s
           \s
            "exit" - close the program
           \s""";

        System.out.println();

        mainLoop:
        while (true)
        {
            String rawCommand;

            Option selectedMapType = Option.MAP_8;
            Option selectedColorMode = Option.NORMAL;

            boolean pass = false;

            do // Prompt user for input until correct
            {
                System.out.print(">>> "); rawCommand = scanner.nextLine();

                if (rawCommand.equalsIgnoreCase("exit"))
                {
                    break mainLoop;
                }

                if (rawCommand.equalsIgnoreCase("help"))
                {
                    System.out.println(manualText);
                    continue;
                }

                try // Validate input
                {
                    Command command = new Command(rawCommand);
                    path = command.getPath();
                    selectedMapType = command.getMapType();
                    selectedColorMode = command.getColorMode();

                    if (Validation.validatePath(path).getStatus() != FileStatus.FILE_OK)
                    {
                        System.out.println("\n" + Validation.validatePath(path).getMessage());
                    }
                    else
                    {
                        pass = true;
                    }
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println("\n" + e.getMessage());
                }

            } while (!pass);

            try // Render and print image
            {
                System.out.println("\n\n");

                images.add(new Image(path, selectedMapType, selectedColorMode));

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
