package main.java;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankTest {
    private Map<Integer, Account> accounts = new LinkedHashMap<>();
    private ExecutorService threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Bank bank = new Bank();

    /**
     * Тест показывающий что не все потоки вовремя заметят что аккаунт уже заблокирован.
     * <p>
     * Чтобы тест заработал, надо подготовить код:
     * <ol>
     * <li>В isFraud заккомментировать все тело и заменить его на return true;
     * <pre>
     *    {@code
     *    public synchronized boolean isFraud(
     *                String fromAccountNum, String toAccountNum, long amount) {
     *        return true;
     *    }
     * }
     * </pre>
     * </li>
     * <li>Поставить задержку {@code BankTest.delayAfterBlockCheck()} после проверки на блокировку
     * в методе {@code transfer}
     * <pre>
     *    {@code
     *    if (accFrom.isBlocked() ... ) {
     *
     *    }
     *    // ...
     *
     *    BankTest.delayAfterBlockCheck();
     *
     *    synchronized (accFrom) {
     *        ...
     *    }
     * }
     * </pre>
     * </li>
     * <p>
     *     Идея теста:
     *     <ol>
     *     <li>Создаем 10 аккаунтов со счетами 1 млн рублей
     *     <li>С первого аккаунта делаем переводы на другие аккануты в несколько потоков.
     *     <li>Поскольку isFraud вернет true, первый же перевод должен заблокировать счет.
     *     <li>Тем самым все остальные переводы должны быть отклонены и сумма баланса уменьшиться только на 100к.
     *
     * @throws InterruptedException
     */
    @Test
    public void test_that_only_one_transaction_will_pass() throws InterruptedException {
        this.createNAccounts(10, 1_000_000);

        for (int i = 1; i < 10; i++) {
            transferParallel(0, i, 100_000);
        }

        threads.shutdown();
        threads.awaitTermination(1, TimeUnit.DAYS);

        Assert.assertEquals(900_000, getBalanceOf(0));
    }

    private void transferParallel(int from, int to, int money) {
        threads.submit(() -> {
                    try {
                        bank.transfer(String.valueOf(from), String.valueOf(to), money);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void createNAccounts(int n, int initialBalance) {
        bank = new Bank();
        for (int i = 0; i < n; i++) {
            bank.addAccount(createAccount(i, initialBalance));
        }
    }

    private Account createAccount(int id, int initial) {
        Account account = new Account(String.valueOf(id), initial);
        accounts.put(id, account);
        return account;
    }

    private long getBalanceOf(int accountId) {
        return accounts.get(accountId).getBalance();
    }

    public static void delayAfterBlockCheck() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

}

