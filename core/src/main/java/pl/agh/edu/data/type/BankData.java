package pl.agh.edu.data.type;

import pl.agh.edu.engine.bank.BankAccountDetails;


public record BankData(
        Integer id,
        String name,
        BankAccountDetails accountDetails
) {
  private static Integer nextId = 1;
  public static Integer getNextId() {
    return nextId++;
  }
}
