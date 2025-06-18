package bitmage.Utils;

public class FileResult
{
    private FileStatus status;
    private String message;

    public FileResult(FileStatus status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public FileStatus getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }
}
