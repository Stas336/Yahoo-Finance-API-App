package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ValueReader
{
    public static String readValue()
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            return bufferedReader.readLine();
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        return null;
    }
}
