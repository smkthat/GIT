import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Loader
{
    public static void main(String[] args) throws IOException {
        Cat murzic = new Cat();
        murzic.name = "murzic";
        Cat vaska = new Cat();
        vaska.name = "vaska";
        Cat snezhok = new Cat();
        snezhok.name = "snezhok";
        Cat ortrud = new Cat();
        ortrud.name = "ortrud";
        Cat zubastik = new Cat();
        zubastik.name = "zubastik";
        Cat hadjit = new Cat();
        hadjit.name = "hadjit";
        Cat gerd = new Cat();
        gerd.name = "gerd";
        Cat murka = new Cat();


        murka.createСlone("murka", murzic.getWeight(), murzic.getKitten());
        Cat mini = new Cat(120.0D, "mini");


        System.out.println("Please enter new cat's name: ");
        String newCatName = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        Cat cat = createCat(newCatName);

        System.out.println("---------------- Cats/kittens weight: --------\n");
        System.out.println(murzic.isKitten() + " " + murzic.getName() + " " + murzic.getWeight());
        System.out.println(murka.isKitten() + " " + murka.getName() + " " + murka.getWeight());
        System.out.println(vaska.isKitten() + " " + vaska.getName() + " " + vaska.getWeight());
        System.out.println(snezhok.isKitten() + " " + snezhok.getName() + " " + snezhok.getWeight());
        System.out.println(ortrud.isKitten() + " " + ortrud.getName() + " " + ortrud.getWeight());
        System.out.println(zubastik.isKitten() + " " + zubastik.getName() + " " + zubastik.getWeight());
        System.out.println(hadjit.isKitten() + " " + hadjit.getName() + " " + hadjit.getWeight());
        System.out.println(gerd.isKitten() + " " + gerd.getName() + " " + gerd.getWeight());
        System.out.println(mini.isKitten() + " " + mini.getName() + " " + mini.getWeight());
        System.out.println(cat.isKitten() + " " + cat.getName() + " " + cat.getWeight());


        System.out.println();
        System.out.println("-------------------- Feed! -------------------");

        if (!murzic.getKitten()) {
            do {
                murzic.feed(1000.0D);
            } while(murzic.getWeight() < 9000.0D);

            System.out.println(murzic.getName() + " " + murzic.getStatus());
        } else {
            do {
                murzic.feed(40.0D);
            } while(murzic.getWeight() < 200.0D);

            System.out.println(murzic.getName() + " " + murzic.getStatus());
        }

        vaska.feed(3000.0D);
        mini.feed(20.0D);
        zubastik.feed(70.0D);
        snezhok.feed(500.0D);


        System.out.println("-------------------- Meow! -------------------");

        do {
            hadjit.meow();
        } while(hadjit.getWeight() > 1000.0D);

        System.out.println(hadjit.getName() + " " + hadjit.getStatus());


        System.out.println("------------------ Cats status: --------------\n");

        System.out.println(murzic.getName() + " " + murzic.getStatus());
        System.out.println(murka.getName() + " " + murka.getStatus());
        System.out.println(vaska.getName() + " " + vaska.getStatus());
        System.out.println(snezhok.getName() + " " + snezhok.getStatus());
        System.out.println(ortrud.getName() + " " + ortrud.getStatus());
        System.out.println(zubastik.getName() + " " + zubastik.getStatus());
        System.out.println(hadjit.getName() + " " + hadjit.getStatus());
        System.out.println(gerd.getName() + " " + gerd.getStatus());
        System.out.println(mini.getName() + " " + mini.getStatus());
        System.out.println(cat.getName() + " " + cat.getStatus());


        System.out.println();

        System.out.println("Cats count - " + Cat.getCount());
        System.out.println("Life Cats - " + Cat.getLiveCount());
        System.out.println("Dead Cats - " + Cat.getDeathCount());
    }

    public static Cat createCat(String name)
    {
        Cat cat = new Cat(name);
        System.out.println("New " + cat.isKitten() + " appeared. His nickname " + cat.getName()
                                + "\nand he weight " + cat.getWeight() + "\n");
        System.out.println("Now " + cat.getName() + " " + cat.getStatus() + "\n");
        return cat;
    }
}
