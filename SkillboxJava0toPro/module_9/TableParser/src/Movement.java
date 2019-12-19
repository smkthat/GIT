public class Movement {
  private String type;
  private String account;
  private String currency;
  private String date;
  private String reference;
  private String description;
  private Double income;
  private Double outcome;

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
  }

  @Override
  public String toString() {
    String tab = "\t";
    return getClass().getSimpleName()
        + tab
        + type
        + tab
        + account
        + tab
        + currency
        + tab
        + date
        + tab
        + reference
        + tab
        + income
        + tab
        + outcome;
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
