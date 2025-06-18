package bitmage;
import bitmage.Exceptions.ImageTooBigException;
import bitmage.Utils.FileStatus;
import bitmage.Utils.Validation;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {

        Scanner scanner = new Scanner(System.in);
        String path;

        do
        {
            System.out.print("Enter image path: "); path = scanner.next();
            if (Validation.validatePath(path).getStatus() != FileStatus.FILE_OK)
            {
                System.out.println("\n" + Validation.validatePath(path).getMessage());
            }
        } while (Validation.validatePath(path).getStatus() != FileStatus.FILE_OK);

        try
        {
            System.out.println("\n\n\n");

            Image img = new Image(path);

            img.print();

            System.out.println("\n\n\n Done.");
        }
        catch (InputMismatchException | IOException | ImageTooBigException e)
        {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Press ENTER to finish.");
        new Scanner(System.in).nextLine();
    }
}
