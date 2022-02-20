package ru.borisov.bank;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class BankStatementParserTest {
    private static final String RESOURCES = "src/main/resources/";
    private final BankStatementParser statementParser = new BankStatementCSVParser();

    @Test
    public void shouldParseOneCorrectLine() {
        final String line = "30-01-2022,-50,Tesco";
        final BankTransaction result = statementParser.parseFrom(line);
        final BankTransaction expected = new BankTransaction(LocalDate.of(2022, Month.JANUARY,30),-50,"Tesco");
        final double tolerance = 0.0d;
        Assert.assertEquals(expected.getDate(),result.getDate());
        Assert.assertEquals(expected.getAmount(),result.getAmount(),tolerance);
        Assert.assertEquals(expected.getDescription(),result.getDescription());
    }
    @Test
    public void BankStatementProcessorTest() throws IOException {
        final double expectedTotalAmount = 6820.0;
        final String fileName = "bank-data-simple.csv";
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions = statementParser.parseLinesFrom(lines);
        BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
        final double tolerance = 0.0d;
        Assert.assertEquals(expectedTotalAmount,bankStatementProcessor.calculateTotalAmount(),tolerance);
    }
}
