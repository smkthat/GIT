import core.*;
import core.Camera;

public class RoadController
{   //создание переменных и подклассов:
    public static double passengerCarMaxWeight = 3500.0; // kg
    public static int passengerCarMaxHeight = 2000; // mm
    public static int controllerMaxHeight = 3500; // mm

    public static int passengerCarPrice = 100; // RUB
    public static int cargoCarPrice = 250; // RUB
    public static int vehicleAdditionalPrice = 200; // RUB

    public static int maxOncomingSpeed = 60; // km/h
    public static int speedFineGrade = 20; // km/h
    public static int finePerGrade = 500; // RUB
    public static int criminalSpeed = 160; // km/h

    public static void main(String[] args)
    {
        for(int i = 0; i < 10; i++) //создание переменной в цикле
        {
            Car car = Camera.getNextCar();
            System.out.println(car);
            System.out.println("Скорость: " + Camera.getCarSpeed(car) + " км/ч");


            /**
             * Проверка на наличие номера в списке номеров нарушителей
             */
            boolean policeCalled = false; //создание переменной
            for(String criminalNumber : Police.getCriminalNumbers())
            {
                String carNumber = car.getNumber();
                if(carNumber.equals(criminalNumber) || (carNumber.equals(criminalNumber) && car.isSpecial())) {
                    Police.call("автомобиль нарушителя с номером " + carNumber);
                    blockWay("не двигайтесь с места! За вами уже выехали!");
                    break;
                }
            }
            if(Police.wasCalled()) {
                continue;
            }

            /**
             * Пропускаем автомобили спецтранспорта
             */
            if(car.isSpecial()) {
                openWay();
                continue;
            }

            /**
             * Проверяем высоту и массу автомобиля, вычисляем стоимость проезда
             */
            int carHeight = car.getHeight(); //создание переменной
            int price = 0; //создание переменной
            if(carHeight > controllerMaxHeight)
            {
                blockWay("высота вашего ТС превышает высоту пропускного пункта!");
                continue;
            }
            else if(carHeight > passengerCarMaxHeight)
            {
                double weight = WeightMeter.getWeight(car); //создание переменной
                //Грузовой автомобиль
                if(weight > passengerCarMaxWeight)
                {
                    price = cargoCarPrice;
                    if(car.hasVehicle()) {
                        price = price + vehicleAdditionalPrice;
                    }
                }
                //Легковой автомобиль
                else {
                    price = passengerCarPrice;
                }
            }
            else {
                price = passengerCarPrice;
            }

            /**
             * Проверка скорости подъезда и выставление штрафа
             */
            int carSpeed = Camera.getCarSpeed(car);
            if(carSpeed > criminalSpeed)
            {
                Police.call("cкорость автомобиля - " + carSpeed + " км/ч, номер - " + car.getNumber());
                blockWay("вы значительно превысили скорость. Ожидайте полицию!");
                continue;
            }
            else if(carSpeed > maxOncomingSpeed && carSpeed > 60)
            {
                int overSpeed = carSpeed - maxOncomingSpeed; //создание переменной
                int totalFine = finePerGrade * (overSpeed / speedFineGrade); //создание переменной
                System.out.println("Вы превысили скорость! Штраф: " + totalFine + " руб.");

                if(totalFine == 0)
                {
                    price = 0;
                }
                else price = price + totalFine;
            }

            /**
             * Отображение суммы к оплате
             */
            System.out.println("Общая сумма к оплате: " + price + " руб.");
        }

    }

    /**
     * Открытие шлагбаума
     */
    public static void openWay()
    {
        System.out.println("Шлагбаум открывается... Счастливого пути!");
    }

    public static void blockWay(String reason)
    {
        System.out.println("Проезд невозможен: " + reason);
    }
}