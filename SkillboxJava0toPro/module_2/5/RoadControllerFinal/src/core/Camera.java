package core;

import java.util.HashMap;

public class Camera
{
    public static HashMap<String, Integer> carsSpeed = new HashMap<>(); //создание переменных

    public static Car getNextCar()
    {
        String randomNumber = Double.toString(Math.random()).substring(2, 5); //создание переменной
        int randomHeight = (int) (1000 + 3500. * Math.random()); //создание переменной
        double randomWeight = 600 + 10000 * Math.random(); //создание переменной
        Car car = new Car(randomNumber, randomHeight, randomWeight, Math.random() > 0.5);
        if(Math.random() < 0.15) {
            car.setIsSpecial();
        }
        Police.resetCalled();

        return car;
    }

    public static int getCarSpeed(Car car)
    {
        String carNumber = car.getNumber();
        if(!carsSpeed.containsKey(carNumber)) {
            carsSpeed.put(carNumber, (int) (180 * Math.random()));
        }
        return carsSpeed.get(carNumber);
    }
}