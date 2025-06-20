package bitmage.Utils;

import bitmage.Enums.FileStatus;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.io.IOException;

public class Validation
{
    private final static String[] allowedExtensions = {"JPG", "jpg", "tiff", "bmp", "BMP", "gif", "GIF", "WBMP", "png", "PNG", "JPEG", "tif", "TIF", "TIFF", "wbmp", "jpeg"};
    private final static char[] allowedChars = {' ', '.', ',', ':', '-', '~', '=', '+', '*', '#', '%', '&', '@', '█'};

    public static String[] getAllowedExtensions()
    {
        return allowedExtensions;
    }

    public static String getFileName(String path)
    {
        String[] parts = path.split("/");
        String fullName = parts[parts.length - 1];

        return fullName.split("\\.")[0];
    }

    public static String getFileExtension(String path)
    {
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }

    // Check if file can be opened and is supported
    public static FileResult validatePath(String path)
    {
        String extension = getFileExtension(path);

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

    // Check if text file can be rendered
    public static FileResult validateTxtFile(String path) throws IOException
    {
        String content = Files.readString(Paths.get(path));

        for (Character c : content.toCharArray())
        {
            if (new String(allowedChars).indexOf(c) == -1)
            {
                return new FileResult(FileStatus.CANNOT_RENDER, "Cannot render this file.");
            }
        }
        return new FileResult(FileStatus.FILE_OK, "Success");
    }
}
