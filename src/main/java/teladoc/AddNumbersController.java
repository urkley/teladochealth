package teladoc;

import java.util.regex.Pattern;

public class AddNumbersController {

    public static String addNumbers(String firstLine, String secondLine) {
        Pattern pattern = Pattern.compile("[\\d\\s.]+");
        if (!pattern.matcher(firstLine).matches() || !pattern.matcher(secondLine).matches()) {
            throw new IllegalArgumentException("Only numbers and spaces are expected");
        }
        String[] numbersFromFirstLine = firstLine.split(" ");
        String[] numbersFromSecondLine = secondLine.split(" ");
        if (numbersFromFirstLine.length != numbersFromSecondLine.length) {
            throw new IllegalArgumentException("The same amount of numbers in both lines is expected");
        }
        StringBuilder resultLine = new StringBuilder();
        for (int i = 0; i < numbersFromFirstLine.length; i++) {
            resultLine.append(addTwoNumbers(numbersFromFirstLine[i], numbersFromSecondLine[i]));
            if (i < numbersFromFirstLine.length - 1) {
                resultLine.append(" ");
            }
        }
        return resultLine.toString();
    }

    private static String addTwoNumbers(String number1, String number2) {
        return addTwoFloatNumbers(number1, number2);
    }

    private static String addTwoFloatNumbers(String number1, String number2) {
        boolean isFirstNumberDecimal = number1.contains(".");
        String firstNumberIntPart = isFirstNumberDecimal ? number1.split("\\.")[0] : number1;
        String firstNumberFraction = isFirstNumberDecimal ? number1.split("\\.")[1] : "0";

        boolean isSecondNumberDecimal = number2.contains(".");
        String secondNumberIntPart = isSecondNumberDecimal ? number2.split("\\.")[0] : number2;
        String secondNumberFraction = isSecondNumberDecimal ? number2.split("\\.")[1] : "0";

        String integerPartsSumResult = addTwoIntegerNumbers(firstNumberIntPart, secondNumberIntPart);
        Fractional fractional = addTwoFractionalsAndReturnCarryIfExists(firstNumberFraction, secondNumberFraction);
        if (fractional.carryExists) {
            integerPartsSumResult = addTwoNumbers(integerPartsSumResult, "1");
        }
        if (Pattern.compile("0+").matcher(fractional.fractionalPart).matches()) {
            return integerPartsSumResult;
        }
        return integerPartsSumResult + "." + fractional.fractionalPart;
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

    private static Fractional addTwoFractionalsAndReturnCarryIfExists(String firstFraction, String secondFraction) {
        if ("0".equals(firstFraction)) {
            return new Fractional(secondFraction, false);
        }
        if ("0".equals(secondFraction)) {
            return new Fractional(firstFraction, false);
        }
        if (firstFraction.length() < secondFraction.length()) {
            String stringForSwitch = firstFraction;
            firstFraction = secondFraction;
            secondFraction = stringForSwitch;
        }
        secondFraction = secondFraction + "0".repeat(firstFraction.length() - secondFraction.length());
        String fractionSum = addTwoIntegerNumbers(firstFraction, secondFraction);
        if (fractionSum.length() > firstFraction.length()) {
            return new Fractional(fractionSum.substring(1), true);
        }
        return new Fractional(fractionSum, false);
    }

    private record Fractional(String fractionalPart, boolean carryExists) {}
}
