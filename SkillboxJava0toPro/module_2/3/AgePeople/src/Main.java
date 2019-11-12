import com.sun.org.apache.xpath.internal.operations.Bool;

public class Main
{
    public static void main(String[] args)
    {
        /*String a = "Misha";
        String b = "Vasya";

        int comparison = a.compareTo(b);
        System.out.println(comparison);

        Boolean comparison1 = a.equals(b);
        System.out.println(comparison1);*/

        /*Boolean vasyaHasLicense = true;
        String name = vasyaHasLicense ? "Vasya" : "Not Vasya";
        System.out.println(name);*/


        int dimaAge = 66;
        int mishaAge = 34;
        int vasyaAge = 18;

        int youngest;
        int middle;
        int oldest;


        //======================================================
        // находим младший и старший возраст

        youngest = (dimaAge < mishaAge) ? dimaAge : mishaAge;
        youngest = (youngest < vasyaAge) ? youngest : vasyaAge;

        oldest = (dimaAge > mishaAge) ? dimaAge : mishaAge;
        oldest = (oldest > vasyaAge) ? oldest : vasyaAge;

        //======================================================
        // находим средний возраст

        if((youngest == dimaAge && oldest == mishaAge) || (youngest == mishaAge && oldest == dimaAge))
        {
            middle = vasyaAge;
        }
        else if ((youngest == vasyaAge && oldest == mishaAge) || (youngest == mishaAge && oldest == vasyaAge))
            {
                middle = dimaAge;
            }
        else middle = mishaAge;

        //======================================================
        // выводм на экран младший возраст и имя его обладателя

        if(youngest == dimaAge)
        {
            System.out.println("Dima is the youngest, his age is " + youngest + ".");
        }

        if(youngest == mishaAge)
        {
            System.out.println("Misha is the youngest, his age is " + youngest + ".");
        }

        if(youngest == vasyaAge)
        {
            System.out.println("Vasya is the youngest, his age is " + youngest + ".");
        }

        //======================================================
        // выводм на экран срдний возраст и имя его обладателя

        if(middle == dimaAge)
        {
            System.out.println("Dima middle age, his age " + middle + ".");
        }

        if(middle == mishaAge)
        {
            System.out.println("Misha middle age, his age " + middle + ".");
        }

        if(middle == vasyaAge)
        {
            System.out.println("Vasya middle age, his age " + middle + ".");
        }

        //======================================================
        // выводм на экран старший возраст и имя его обладателя

        if(oldest == dimaAge)
        {
            System.out.println("Dima is the oldest, his age is " + oldest + ".\n");
        }

        if(oldest == mishaAge)
        {
            System.out.println("Misha is the oldest, his age is " + oldest + ".\n");
        }

        if(oldest == vasyaAge)
        {
            System.out.println("Vasya is the oldest, his age is " + oldest + ".\n");
        }

        //========================================================
        // выводм на экран возроста

        System.out.println("Most old: " + oldest);
        System.out.println("Most young: " + youngest);
        System.out.println("Middle: " + middle);

    }
}
