package bitmage.Utils;

import bitmage.Enums.Option;

import java.util.Arrays;

public class Command
{
    private final String rawCommand;
    private Option mapTypeOption = Option.MAP_8; // 8 or 16 char map
    private Option colorModeOption = Option.NORMAL; // Normal or inverted display
    private String path;

    public Command(String rawCommand)
    {
        this.rawCommand = rawCommand;
        try
        {
            decodeCommand(this.rawCommand);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Option getMapTypeOption() { return mapTypeOption; }
    public Option getColorModeOption() { return colorModeOption; }
    public String getPath() { return path; }

    // Extract options and filepath
    private void decodeCommand(String rawCommand)
    {
        String[] parts = rawCommand.split(" ");

        // Delete empty strings
        parts = Arrays.stream(parts)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        if (!parts[0].equals("gen"))
        {
            throw new IllegalArgumentException("Unknown command: \"" + parts[0] + "\"");
        }

        boolean optionSegment = true;
        StringBuilder pathBuffer = new StringBuilder();

        for (int i = 1; i < parts.length; i++)
        {
            // Detect and interpret options
            if (optionSegment && parts[i].charAt(0) == '-' && parts[i].length() == 2)
            {
                switch (parts[i].charAt(1))
                {
                    case 'm':
                        mapTypeOption = Option.MAP_16;
                        break;
                    case 'i':
                        colorModeOption = Option.INVERTED;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown option: \"" + parts[i] + "\"");
                }
            }
            else
            {
                optionSegment = false;
                pathBuffer.append(parts[i]);
            }
        }

        path = pathBuffer.toString();
    }
}
