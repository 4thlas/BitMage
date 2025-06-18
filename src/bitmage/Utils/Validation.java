package bitmage.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation
{
    private final static String[] allowedExtensions = {"JPG", "jpg", "tiff", "bmp", "BMP", "gif", "GIF", "WBMP", "png", "PNG", "JPEG", "tif", "TIF", "TIFF", "wbmp", "jpeg"};

    public static String[] getAllowedExtensions()
    {
        return allowedExtensions;
    }

    public static String getFileExtension(String fileName)
    {
        String[] parts = fileName.split("\\.");
        return parts[parts.length - 1];
    }

    public static FileResult validatePath(String path)
    {
        final Pattern pathPattern = Pattern.compile("^(?<ParentPath>(?:[a-zA-Z]\\:|\\\\\\\\[\\w\\s\\.]+\\\\[\\w\\s\\.$]+)\\\\(?:[\\w\\s\\.]+\\\\)*)(?<BaseName>[\\w\\s\\.]*?)$");
        Matcher matcher = pathPattern.matcher(path);

        String extension = getFileExtension(path);

        if (!matcher.find())
        {
            return new FileResult(FileStatus.INVALID_PATH_SYNTAX, "Path syntax is invalid.");
        }

        File file = new File(path);

        if (!file.exists())
        {
            return new FileResult(FileStatus.FILE_NOT_FOUND, "File not found.");
        }
        if (!file.isFile())
        {
            return new FileResult(FileStatus.IS_NOT_FILE, "Path is not a file.");
        }
        if (!file.canRead())
        {
            return new FileResult(FileStatus.FILE_ACCESS_DENIED, "No permission to read the file.");
        }
        if (!Arrays.asList(allowedExtensions).contains(extension))
        {
            return new FileResult(FileStatus.INVALID_FILE_EXTENSION, "Given extension is not supported");
        }

        return new FileResult(FileStatus.FILE_OK, "Success");
    }


}
