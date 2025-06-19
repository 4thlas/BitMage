package bitmage;
import bitmage.Utils.FileResult;
import bitmage.Utils.FileStatus;
import bitmage.Utils.Validation;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

class Image
{
    private final String path;
    private final String[] asciiMap = {"  ", "··", "--", "++", "**", "%%", "##", "@@"};

    private String srcExtension; // Holds the source file extension, required for print()
    private ArrayList<ArrayList<String>> renderedImage = null;

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
    private File openFile(String path) throws IOException {
        FileResult result = Validation.validatePath(path);
        srcExtension = Validation.getFileExtension(path);

        switch (result.getStatus())
        {
            case FILE_NOT_FOUND:
            case INVALID_FILE_EXTENSION:
            case IS_NOT_FILE:
            case FILE_ACCESS_DENIED:
                throw new IOException(result.getMessage());
            case FILE_OK:
                break;
            default:
                throw new IOException("Unknown Error");
        }

        File file = new File(path);

        return file;
    }

    // Render image from file
    private ArrayList<ArrayList<String>> render(File rawImg) throws IOException
    {
        //TODO: handle .txt files

        ArrayList<ArrayList<String>> renderBuffer = new ArrayList<>();

        BufferedImage rawImage = ImageIO.read(rawImg);

        int width = rawImage.getWidth();
        int height = rawImage.getHeight();

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
            renderBuffer.add(row);
        }

        return renderBuffer;
    }

    public ArrayList<ArrayList<String>> getRenderedImage()
    {
        return renderedImage;
    }

    // Print rendered image
    public void print() throws IOException, FileNotFoundException
    {
        if (renderedImage == null) // Render image if not rendered
        {
            File rawImg = openFile(path);
            renderedImage = render(rawImg);
        }


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
