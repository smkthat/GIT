import com.sun.org.apache.xpath.internal.operations.Bool;

public class Loader
{
    public static void main(String[] args)
    {
        Integer milkAmount = 1000; // ml
        Integer powderAmount = 400; // g
        Integer eggsCount = 5; // items
        Integer sugarAmount = 15; // g
        Integer oilAmount = 30; // ml
        Integer appleCount = 8; // items

        //======================= Условия ============================

        Boolean isPancakes = powderAmount >= 400 && sugarAmount >= 10 && milkAmount >= 1000 && oilAmount >= 30;
        Boolean isOmelette = milkAmount >= 300 && powderAmount >= 5 && eggsCount >= 5;
        Boolean isApplePie = appleCount >= 3 && milkAmount >= 100 && powderAmount >= 300 && eggsCount >= 4;
        Boolean available =  isPancakes && isOmelette && isApplePie;
        Boolean unAvailable = !isPancakes && !isOmelette && !isApplePie;

        if(available)
            System.out.println("Доступны все блюда!\n");

        else if(unAvailable)
            System.out.println("Недостаточно продуктов для приготовления блюда!\n");

        if(isPancakes)
            System.out.println("Для приготовления Pancakes\nнам понадобится:\n" +
                    "powder - 400 g, sugar - 10 g, milk - 1 l, oil - 30 ml\n");

        if(isOmelette)
            System.out.println("Для приготовления Omelette\nнам понадобится:\n" +
                    "milk - 300 ml, powder - 5 g, eggs - 5\n");

        if(isApplePie)
            System.out.println("Для приготовления Apple pie\nнам понадобится:\n" +
                    "apples - 3, milk - 100 ml, powder - 300 g, eggs - 4\n");

        /*====================== Рецепты =======================

        powder - 400 g, sugar - 10 g, milk - 1 l, oil - 30 ml
        System.out.println("Pancakes");

        milk - 300 ml, powder - 5 g, eggs - 5
        System.out.println("Omelette");

        apples - 3, milk - 100 ml, powder - 300 g, eggs - 4
        System.out.println("Apple pie");

        //====================== Наличие =======================*/

        System.out.println("Продукты в наличии:\nmilk - " + milkAmount +", powder - " + powderAmount +
                ", eggs - " + eggsCount + ", sugar - " + sugarAmount +
                ", oil - " + oilAmount + ", apples - " + appleCount + ".");
    }
}