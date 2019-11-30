import com.skillbox.airport.Airport;
import java.util.Date;


public class Main {
  private static final long MILLS_PER_HOUR = 3600000;
  public static void main(String[] args) {

    Airport airport = Airport.getInstance();
    airport.getTerminals().forEach(terminal ->
        terminal.getFlights().stream().filter(flight ->
            flight.getType().toString().equals("DEPARTURE") &&
            flight.getDate().after(new Date()) &&
            flight.getDate().before(new Date(new Date().getTime() + MILLS_PER_HOUR * 2)))
        .forEach(System.out::println)
    );

  }
}
