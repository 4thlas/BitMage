package bitmage;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


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

    // Open and prepare image from file
    private BufferedImage openFile(String path) throws IOException, FileNotFoundException
    {
        File file = new File(path);
        if (!file.exists() || !file.isFile())
        {
            throw new FileNotFoundException("File not found.");
        }

        return ImageIO.read(file);
    }

    // Render image from file
    private void render(BufferedImage rawImage)
    {
        int width = rawImage.getWidth();
        int height = rawImage.getHeight();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = rawImage.getRGB(x, y);

                // Read pixel properties
                int alpha = (pixel >> 24) & 0xff;
                int red   = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue  = pixel & 0xff;

                System.out.println("Pixel at (" + x + "," + y + "): A=" + alpha + " R=" + red + " G=" + green + " B=" + blue);
            }
        }
        //return image;
    }

    // Print rendered image
    public void print() throws IOException, FileNotFoundException
    {
        BufferedImage rawImage = openFile(path);
        render(rawImage);
        //ArrayList<ArrayList<Character>> renderedImage = render(rawImage);
    }
}
