package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Calculator {

    private static final Integer PREVIOUS_NUMBER = 0;
    private static final Integer NEXT_NUMBER = 1;

    /**
     * Retorna o resultado da função matemática em formato de {@link String}
     * @param mathFunction
     * @return {@link String}
     */
    public static String getResult(StringBuilder mathFunction) {
        if(isNull(mathFunction)) return "0";

        Integer symbolId = nonNull(mathFunction) ? getSymbol(mathFunction) : null;
        Character operationChar = nonNull(symbolId) ? mathFunction.charAt(symbolId) : null;

        if (nonNull(symbolId)) {
            StringBuilder previous = getNumber(mathFunction, symbolId, PREVIOUS_NUMBER);
            StringBuilder next = getNumber(mathFunction, symbolId, NEXT_NUMBER);

            BigDecimal a = new BigDecimal(previous.reverse().toString());
            BigDecimal b = new BigDecimal(next.toString());
            BigDecimal result = doMath(a, b, operationChar);

            Integer startIndex = symbolId - previous.length();
            Integer endIndex = symbolId + next.length() + 1;
            mathFunction.replace(startIndex, endIndex, String.valueOf(result));

            return getResult(mathFunction);
        }

        return String.valueOf(new BigDecimal(mathFunction.toString()).setScale(6, RoundingMode.HALF_UP).stripTrailingZeros());
    }

    /**
     * Retorna o valor que está atrás ou depois do sinal de função, dependendo do {@link Integer} op
     * @param mathFunction
     * @param symbolId index do simbolo no {@link StringBuilder} mathFunction
     * @param op decide se o valor para pegar está antes ou depois do sinal de função
     * @return valor numérico como {@link StringBuilder}
     */
    public static StringBuilder getNumber(StringBuilder mathFunction, Integer symbolId, Integer op) {
        StringBuilder sb = new StringBuilder();

        Integer i = symbolId;
        if (op.equals(PREVIOUS_NUMBER)) {
            while (i > 0) {
                Character c = mathFunction.charAt(--i);
                if ((Character.isDigit(c) || c == ',' || c == '.') || (i == 0 && c == '-')) {
                    sb.append(mathFunction.charAt(i));
                } else{
                    return sb;
                }
            }
        } else if(op.equals(NEXT_NUMBER)) {
            while (mathFunction.length() > i + 1) {
                Character c = mathFunction.charAt(++i);
                if (Character.isDigit(c) || c == ',' || c == '.') {
                    sb.append(mathFunction.charAt(i));
                } else {
                    return sb;
                }
            }
        }

        return sb;
    }

    /**
     * Retorna o index dentro da {@link StringBuilder} mathFunction do símbolo da função
     * Primeiro verifica se há multiplicações ou divisões a serem feitas, se não retorna a última função de soma ou subtração
     * @param mathFunction
     * @return
     */
    public static Integer getSymbol(StringBuilder mathFunction) {
        Integer operationId = null;
        for (int i = 0; i < mathFunction.length(); i++) {
            Character c = mathFunction.charAt(i);
            if (c.equals('*') || c.equals('/') || c.equals('%')) {
                return i;
            } else if((c.equals('-') || c.equals('+')) && isNull(operationId) && i != 0) {
                operationId = i;
            }
        }

        return operationId;
    }

    /**
     * Retorna o resultado da função {@link Double} a e {@link Double} b sobre a função {@link Character} symbol
     * @param a
     * @param b
     * @param symbol
     * @return
     */
    public static BigDecimal doMath(BigDecimal a, BigDecimal b, Character symbol) {
        switch(symbol) {
            case '*': return a.multiply(b);
            case '/': return a.divide(b, 32, RoundingMode.HALF_UP);
            case '+': return a.add(b);
            case '-': return a.subtract(b);
            case '%': return (a.multiply(b)).divide(new BigDecimal("100"), RoundingMode.HALF_UP).stripTrailingZeros();
            default: return BigDecimal.ZERO;
        }
    }
}
