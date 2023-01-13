package teladoc;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddNumbersTestTask {

    private static final Pattern patternDigitsAndSpacesOnly = Pattern.compile("[\\d\\s.]+");
    private static final Pattern patternZeroOnly = Pattern.compile("0+");

    public static String addNumbers(String firstLine, String secondLine) {
        if (!patternDigitsAndSpacesOnly.matcher(firstLine).matches() || !patternDigitsAndSpacesOnly.matcher(secondLine).matches()) {
            throw new IllegalArgumentException("Only numbers and spaces are expected");
        }
        String[] numbersFromFirstLine = firstLine.split(" ");
        String[] numbersFromSecondLine = secondLine.split(" ");
        if (numbersFromFirstLine.length != numbersFromSecondLine.length) {
            throw new IllegalArgumentException("The same amount of numbers in both lines is expected");
        }
        return IntStream.range(0, numbersFromFirstLine.length)
                .mapToObj(i -> addTwoFloatNumbers(numbersFromFirstLine[i].trim(), numbersFromSecondLine[i].trim()))
                .collect(Collectors.joining(" "));
    }

    private static String addTwoFloatNumbers(String number1, String number2) {
        DecimalNumber decimal1 = DecimalNumber.from(number1);
        DecimalNumber decimal2 = DecimalNumber.from(number2);

        String integerPartsSumResult = addTwoIntegerNumbers(decimal1.intPart(), decimal2.intPart());
        Fraction fraction = addTwoFractionsAndReturnCarryIfExists(decimal1.fraction(), decimal2.fraction());
        if (fraction.carryExists()) {
            integerPartsSumResult = addTwoIntegerNumbers(integerPartsSumResult, "1");
        }
        if (patternZeroOnly.matcher(fraction.value()).matches()) {
            return integerPartsSumResult;
        }
        return integerPartsSumResult + "." + fraction.value();
    }

    private static String addTwoIntegerNumbers(String number1, String number2) {
        if (number1.length() < number2.length()) {
            String stringForSwitch = number1;
            number1 = number2;
            number2 = stringForSwitch;
        }
        int diffBetweenNumberLength = number1.length() - number2.length();
        StringBuilder reversedResultNumber = new StringBuilder();
        int carry = 0;
        for (int i = number2.length() - 1; i >= 0; i--) {
            int sum = (number1.charAt(i + diffBetweenNumberLength) - '0') + (number2.charAt(i) - '0') + carry;
            reversedResultNumber.append(sum % 10);
            carry = sum / 10;
        }
        for (int i = number1.length() - number2.length() - 1; i >= 0; i--) {
            int sum = (number1.charAt(i) - '0') + carry;
            reversedResultNumber.append(sum % 10);
            carry = sum / 10;
        }
        if (carry > 0) {
            reversedResultNumber.append("1");
        }
        return reversedResultNumber.reverse().toString();
    }

    private static Fraction addTwoFractionsAndReturnCarryIfExists(String firstFraction, String secondFraction) {
        if ("0".equals(firstFraction)) {
            return new Fraction(secondFraction, false);
        }
        if ("0".equals(secondFraction)) {
            return new Fraction(firstFraction, false);
        }
        if (firstFraction.length() < secondFraction.length()) {
            String stringForSwitch = firstFraction;
            firstFraction = secondFraction;
            secondFraction = stringForSwitch;
        }
        secondFraction = secondFraction + "0".repeat(firstFraction.length() - secondFraction.length());
        String fractionSum = addTwoIntegerNumbers(firstFraction, secondFraction);
        if (fractionSum.length() > firstFraction.length()) {
            return new Fraction(fractionSum.substring(1), true);
        }
        return new Fraction(fractionSum, false);
    }

    private record DecimalNumber(String intPart, String fraction) {

        public static DecimalNumber from(String number) {
            String[] decimalParts = number.split("\\.");
            if (decimalParts.length == 1) {
                return new DecimalNumber(number, "0");
            }
            if (decimalParts.length > 2) {
                throw new IllegalArgumentException("Invalid format of number " + number);
            }
            return new DecimalNumber(decimalParts[0], decimalParts[1]);
        }
    }
    private record Fraction(String value, boolean carryExists) {}
}
