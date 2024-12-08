package Obj;

public class BaseConverter {
    public static class Result {
        public String binary;
        public String decimal;
        public String hexadecimal;
        public String octal;
    }

    public static Result convert(String input, String base) throws IllegalArgumentException {
        int baseValue = switch (base) {
            case "Binary" -> 2;
            case "Decimal" -> 10;
            case "Hexadecimal" -> 16;
            case "Octal" -> 8;
            default -> throw new IllegalArgumentException("Unexpected base selection");
        };

        int decimalValue = Integer.parseInt(input, baseValue);

        Result result = new Result();
        result.binary = Integer.toBinaryString(decimalValue);
        result.decimal = String.valueOf(decimalValue);
        result.hexadecimal = Integer.toHexString(decimalValue).toUpperCase();
        result.octal = Integer.toOctalString(decimalValue);

        return result;
    }
}
