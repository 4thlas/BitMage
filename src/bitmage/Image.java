package bitmage;

import bitmage.Utils.FileResult;
import bitmage.Enums.FileStatus;
import bitmage.Enums.Option;
import bitmage.Utils.Validation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;

class Image
{
    private final String path;
    private final Option mapType;
    private final Option colorMode;

    private final Map<String, String[]> asciiMaps = Map.of(
            "map8", new String[] {"  ", "··", "--", "++", "**", "%%", "##", "@@"},
            "map16", new String[] { "  ", "..", "::", "--", "==", "++", "**", "oo", "OO", "##", "%%", "&&", "88", "BB", "@@", "██" }
    );

    private String srcFileName; // Holds the source file name
    private String srcFileExt; // Holds the source file extension

    private ArrayList<ArrayList<String>> renderedImage = null;

    public Image(String path) throws IOException
    {
        this(path, Option.MAP_8, Option.NORMAL);
    }

    public Image(String path, Option mapType) throws IOException
    {
        this(path, mapType, Option.NORMAL);
    }

    public Image(String path, Option mapType, Option colorMode) throws IOException
    {
        if (Validation.validatePath(path).getStatus() == FileStatus.FILE_OK)
        {
            this.path = path.replace('\\', '/');
            this.mapType = mapType;
            this.colorMode = colorMode;
        }
        else
        {
            throw new IOException(Validation.validatePath(path).getMessage());
        }
    }

    // Open and prepare image from file
    private File openFile(String path) throws IOException
    {
        FileResult result = Validation.validatePath(path);

        srcFileName = Validation.getFileName(path);
        srcFileExt = Validation.getFileExtension(path);

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
    private ArrayList<ArrayList<String>> render(File rawImg, Option mapType, Option colorMode) throws IOException
    {
        //TODO: handle .txt files

        String[] usedMap = (mapType == Option.MAP_16) ? asciiMaps.get("map16") : asciiMaps.get("map8");

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
                int density = (int)(brightness / 256 * usedMap.length);

                String pixelChar;

                if (colorMode == Option.NORMAL)
                {
                    pixelChar = usedMap[Math.min(density, usedMap.length - 1)];
                }
                else
                {
                    // Invert id's
                    int charId = Math.min(density, usedMap.length - 1);
                    int invCharId = usedMap.length - 1 - charId;
                    pixelChar = usedMap[invCharId];
                }

                row.add(pixelChar);
            }
            renderBuffer.add(row);
        }

        return renderBuffer;
    }

    public ArrayList<ArrayList<String>> getRenderedImage() { return renderedImage; }

    // Print rendered image
    public void print() throws IOException
    {
        if (renderedImage == null) // Render image if not rendered
        {
            File rawImg = openFile(path);
            renderedImage = render(rawImg, mapType, colorMode);
        }

        int width = renderedImage.get(0).size();

        // Print image row by row
        for (ArrayList<String> row : renderedImage)
        {
            for (int x = 0; x < width; x++)
            {
                System.out.print(row.get(x));
            }
            System.out.print("\n");
        }
    }
}
