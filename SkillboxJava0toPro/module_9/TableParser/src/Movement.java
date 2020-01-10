import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movement {

  private String type;
  private String account;
  private String currency;
  private String date;
  private String reference;
  private String description;
  private Double income;
  private Double outcome;

  private String partner;

  public Movement(
      String type,
      String account,
      String currency,
      String date,
      String reference,
      String description,
      Double income,
      Double outcome) {
    this.type = type;
    this.account = account;
    this.currency = currency;
    this.date = date;
    this.reference = reference;
    this.description = description;
    this.income = income;
    this.outcome = outcome;
    this.partner = getPartnerFromDescription();
  }

  private String getPartnerFromDescription() {
    String str;
    String pattern = "\\d{1,2}\\.\\d{1,2}\\.\\d{2}";
    Matcher matcher = Pattern.compile(pattern).matcher(description);

    if (matcher.find()) {
      str = description.substring(0, matcher.start()).trim();
      pattern = "\\b\\w+>\\w+$|\\w+\\s\\w+\\s\\w+$|\\w+\\s\\w+$|\\w+$";
      matcher = Pattern.compile(pattern).matcher(str);
      if (matcher.find()) {
        return matcher.group(0);
      }
    }

    System.err.println("Partner not found at: \"" + description + "\"");
    return "unknown";
  }

  @Override
  public String toString() {
    String tab = "\t";
    StringJoiner joiner = new StringJoiner(tab);
    joiner
        .add(getClass().getSimpleName())
        .add(type)
        .add(account)
        .add(currency)
        .add(date)
        .add(reference)
        .add(income.toString())
        .add(outcome.toString());
    return joiner.toString();
  }

  public String getPartner() {
    return partner;
  }

  public String getType() {
    return type;
  }

  public String getAccount() {
    return account;
  }

  public String getCurrency() {
    return currency;
  }

  public String getDate() {
    return date;
  }

  public String getReference() {
    return reference;
  }

  public String getDescription() {
    return description;
  }

  public Double getIncome() {
    return income;
  }

  public Double getOutcome() {
    return outcome;
  }
}
