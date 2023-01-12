package teladoc;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AddNumbersControllerTest {

    @Test
    public void testAddSeveralOneDigitNumbers() {
        String resultString = AddNumbersController.addNumbers("1 2 3 4", "3 2 1 0");
        Assert.assertEquals(resultString, "4 4 4 4");
    }

    @Test
    public void testAddSeveralDigitsNumbers() {
        String resultString = AddNumbersController.addNumbers("123 456 789", "11 22 33");
        Assert.assertEquals(resultString, "134 478 822");
    }

    @Test
    public void testAddTwoZeros() {
        String resultString = AddNumbersController.addNumbers("0", "0");
        Assert.assertEquals(resultString, "0");
    }

    @Test
    public void testAddDecimalOnlyNumbers() {
        String resultString = AddNumbersController.addNumbers("1234567.8901 2.345", "12.34 2345678901.2");
        Assert.assertEquals(resultString, "1234580.2301 2345678903.545");
    }

    @Test
    public void testAddDecimalAndIntegerNumbers() {
        String resultString = AddNumbersController.addNumbers("99 2.345", "1.19876 2345678901");
        Assert.assertEquals(resultString, "100.19876 2345678903.345");
    }

    @Test
    public void testAddBigIntegers() {
        String resultString = AddNumbersController.addNumbers("123456789012345678901 23456789", "12345678 234567890123456789012");
        Assert.assertEquals(resultString, "123456789012358024579 234567890123480245801");
    }

    @Test
    public void testAddBigDecimals() {
        String actualResult = AddNumbersController.addNumbers(
                "123943583485279357735499734597397547934573974579.34583485638456",
                "754279797957493279532749797239457979327957932795.9734757374957934795"
        );
        String expectedResult = "878223381442772637268249531836855527262531907375.3193105938803534795";
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testAddEmptyLinesThrowsException() {
        Assert.assertThrows(
                "Only numbers and spaces are expected",
                IllegalArgumentException.class,
                () -> AddNumbersController.addNumbers("", "")
        );
    }

    @Test
    public void firstNotValidLineThrowsException() {
        Assert.assertThrows(
                "Only numbers and spaces are expected",
                IllegalArgumentException.class,
                () -> AddNumbersController.addNumbers("1 2 a", "3 2 1")
        );
    }

    @Test
    public void secondNotValidLineThrowsException() {
        Assert.assertThrows(
                "Only numbers and spaces are expected",
                IllegalArgumentException.class,
                () -> AddNumbersController.addNumbers("1 2 3", "? 2 1")
        );
    }

    @Test
    public void addDifferentAmountOfNumbersInLineThrowsException() {
        Assert.assertThrows(
                "The same amount of numbers in both lines is expected",
                IllegalArgumentException.class,
                () -> AddNumbersController.addNumbers("1 2", "3 2 1")
        );
    }
}