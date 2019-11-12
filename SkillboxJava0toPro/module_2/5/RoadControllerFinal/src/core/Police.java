package core;

import java.util.HashSet;

/**
 * Created by Danya on 24.08.2015.
 */
public class Police
{
    private static boolean called = false; //создание переменной
    private static HashSet<String> criminalNumbers;

    public static HashSet<String> getCriminalNumbers() //создание переменной
    {
        if(criminalNumbers != null) {
            return criminalNumbers;
        }
        criminalNumbers = new HashSet<>();
        int count = (int) (200 * Math.random()); //создание переменной
        for(int i = 0; i < count; i++) //создание переменной в цикле
        {
            String randomNumber = Double.toString(Math.random()).substring(2, 5); //создание переменной
            criminalNumbers.add(randomNumber);
        }
        return criminalNumbers;
    }

    public static void call(String message)
    {
        called = true;
        System.out.println("Вызов полиции... Причина: " + message);
    }

    public static boolean wasCalled()
    {
        return called;
    }

    //=======================================

    static void resetCalled()
    {
        called = false;
    }
}
