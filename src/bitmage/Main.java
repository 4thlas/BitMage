package bitmage;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    public static boolean validatePath(String path)
    {
        final Pattern pathPattern = Pattern.compile("^[a-zA-Z]:\\\\(?:[^\\\\/:*?\"<>|\\r\\n]+\\\\)*[^\\\\/:*?\"<>|\\r\\n]+\\.(?i)(png|jpe?g|bmp|gif|wbm)$");

        Matcher matcher = pathPattern.matcher(path);
        return matcher.find();
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String path;

        do
        {
            System.out.print("Enter image path: "); path = scanner.next();
            if (!validatePath(path))
            {
                System.out.println("\nInvalid path syntax. Enter correct path.");
            }
        } while (!validatePath(path));

        try
        {
            Image img = new Image(path);

            img.print();
        }
        catch (InputMismatchException | IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        catch (ImageTooBigException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
