package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class TratamentoErros {

    void main() {
        String arquivo = "data/paysim_with_bad_data.csv";
        Path path = Paths.get(arquivo);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<Transaction> lista = lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::processarLinha)
                    .toList();


            IO.println("");
            IO.println("");
            for (Transaction transaction : lista) {
                IO.println(transaction);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction processarLinha(String linha) {
        Transaction transaction = null;
        String[] columns = linha.split(",");
        try {
            IO.println(columns[1]);
            transaction = new Transaction(//
                    getValorStep(columns[0]), //
                    EnumTipoTransacao.getTipoTransacao(columns[1]),//
                    getValorValido("amount", new BigDecimal(columns[2])),//
                    new Customer(columns[3], //
                            getValorValido("oldbalanceOrg",new BigDecimal(columns[4])),
                            getValorValido("newbalanceOrig",new BigDecimal(columns[5]))),//
                    new ClienteDestino(columns[6],//
                            getValorValido("oldbalanceDest",new BigDecimal(columns[7])),
                            getValorValido("newbalanceDest",new BigDecimal(columns[8]))),//
                    "1".equals(columns[9]),//
                    "1".equals(columns[10]));
        } catch (Exception e) {
            IO.println(e.getMessage());
        }
        return transaction;
    }

    private Integer getValorStep(Object valor) {
        Integer step = null;
        isNull(valor);
        try {
            step = Integer.valueOf(valor.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("O valor informado não é um inteiro. Valor informado: " + valor);
        }

        if (Integer.signum(step) == -1) {
            throw new RuntimeException("O step informado é inválido. O step não pode ser um número negativo.  Valor informado: " + valor);
        } else if (Integer.signum(step) == 0) {
            throw new RuntimeException("O step informado é inválido. O step não pode ser igual a 0.  Valor informado: " + valor);
        }

        return step;
    }

    private BigDecimal getValorValido(String campo, Object valor) {
        BigDecimal valorValido = null;
        isNull(valor);

        try {
            valorValido = new BigDecimal(valor.toString());
        } catch (Exception e) {
            throw new RuntimeException("O valor informado para " + campo + " é inválido. Valor informado: " + valor);
        }

        return valorValido;

    }

    private static void isNull(Object valor) {
        if (Objects.isNull(valor) || "".equals(valor.toString())) {
            throw new RuntimeException("Valor informando está nulo ou é inválido. Valor informado: " + valor);
        }
    }


}
