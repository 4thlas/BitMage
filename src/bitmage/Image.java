package bitmage;
import bitmage.Exceptions.ImageTooBigException;
import bitmage.Utils.FileResult;
import bitmage.Utils.FileStatus;
import bitmage.Utils.Validation;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Image
{
    private final String path;
    private final String[] asciiMap = {"  ", "··", "--", "++", "**", "%%", "##", "@@"};
    //private final int MAX_WIDTH = 256;

    private ArrayList<ArrayList<String>> renderedImage;

    public Image(String path) throws IOException
    {
        if (Validation.validatePath(path).getStatus() == FileStatus.FILE_OK)
        {
            this.path = path;
        }
        else
        {
            throw new IOException(Validation.validatePath(path).getMessage());
        }
    }

    // Open and prepare image from file
    private BufferedImage openFile(String path) throws IOException {
        FileResult result = Validation.validatePath(path);

        switch (result.getStatus())
        {
            case FILE_NOT_FOUND:
            case INVALID_FILE_EXTENSION:
            case IS_NOT_FILE:
            case INVALID_PATH_SYNTAX:
            case FILE_ACCESS_DENIED:
                throw new IOException(result.getMessage());
            case FILE_OK:
                break;
            default:
                throw new IOException("Unknown Error");
        }

        File file = new File(path);

        //if (ImageIO.read(file).getWidth() > MAX_WIDTH)
        //{
         //   throw new ImageTooBigException("Maximum allowed image width is "+ MAX_WIDTH +" px.");
        //}

        return ImageIO.read(file);
    }

    // Render image from file
    private ArrayList<ArrayList<String>> render(BufferedImage rawImage)
    {
        int width = rawImage.getWidth();
        int height = rawImage.getHeight();

        ArrayList<ArrayList<String>> renderedImg = new ArrayList<>();

        for (int y = 0; y < height; y++)
        {
            ArrayList<String> row = new ArrayList<>();

            for (int x = 0; x < width; x++)
            {
                int pixel = rawImage.getRGB(x, y);

                // Read pixel properties
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue  = pixel & 0xff;

                // Set the brightness of a pixel with weighted average
                double brightness = 0.2126 * red + 0.7152 * green + 0.0722 * blue;

                // Scale the brightness value to ASCII map array size and convert it to a char
                int brightnessIndex = (int)(brightness / 256 * asciiMap.length);
                String pixelChar = asciiMap[Math.min(brightnessIndex, asciiMap.length - 1)];

                row.add(pixelChar);
            }
            renderedImg.add(row);
        }
        return renderedImg;
    }

    public ArrayList<ArrayList<String>> getRenderedImage()
    {
        return renderedImage;
    }

    // Print rendered image
    public void print() throws IOException, FileNotFoundException
    {
        BufferedImage rawImage = openFile(path);
        renderedImage = render(rawImage);

        int width = renderedImage.get(0).size();
        int height = renderedImage.size();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                System.out.print(renderedImage.get(y).get(x));
            }
            System.out.print("\n");
        }
    }
}
