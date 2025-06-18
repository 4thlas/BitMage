package bitmage;

public class ImageTooBigException extends RuntimeException {
    public ImageTooBigException(String message)
    {
        super(message);
    }
}
