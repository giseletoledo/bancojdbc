package br.com.bancodigitaljdbc.utils;

public class CPFValidator {
    public static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remove pontos e traço
        if (cpf.length() != 11) return false;

        int soma1 = 0, soma2 = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma1 += digito * (10 - i);
            soma2 += digito * (11 - i);
        }

        int resto1 = (soma1 * 10) % 11;
        if (resto1 == 10) resto1 = 0;

        soma2 += resto1 * 2;
        int resto2 = (soma2 * 10) % 11;
        if (resto2 == 10) resto2 = 0;

        return resto1 == Character.getNumericValue(cpf.charAt(9)) &&
               resto2 == Character.getNumericValue(cpf.charAt(10));
    }
}

