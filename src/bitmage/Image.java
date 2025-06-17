package bitmage;
import java.util.InputMismatchException;

class Image
{
    private final String path;

    public Image(String path)
    {
        if (Main.validatePath(path))
        {
            this.path = path;
        }
        else
        {
            throw new InputMismatchException("Invalid path syntax.");
        }
    }
}
