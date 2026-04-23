package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public class FraudAnalyzer {
    public void main() {
        String arquivo = "data/PS_20174392719_1491204439457_log.csv";
        Path path = Paths.get(arquivo);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<Transaction> lista = lines.stream()
                    .skip(1)
                    .limit(50000)
                    .map(this::processarLinha)
                    .toList();


            IO.println("");
            IO.println("");
//            for (Transaction transaction : lista) {
//                IO.println(transaction);
//            }


            //1. Total de Fraudes: 100
            long totalFraudes = lista.stream().filter(Transaction::isFraud).count();
            IO.println("1. Total de Fraudes: " + totalFraudes);

            //2. Top 3 Fraudes de Maior Valor:
            List<Transaction> listaMaiores = lista.stream().filter(Transaction::isFraud)//
                            .sorted(Comparator.comparing(Transaction::getAmount).reversed())//
                    .limit(3)
                    .toList();

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

            IO.println("2. Top 3 Fraudes de Maior Valor: ");
            listaMaiores.forEach(t -> IO.println(nf.format(t.getAmount())));

            //3. Clientes Suspeitos (5 maiores):
            List<Transaction> listaCincoMaiores = lista.stream().filter(Transaction::isFraud)//
                    .sorted(Comparator.comparing(Transaction::getAmount).reversed())//
                    .limit(5)
                    .toList();

            Set<String> maioresSuspeitos = new HashSet<>();
            for (Transaction t: listaCincoMaiores) {
                maioresSuspeitos.add(t.getCustomer().getNameOrig());
            }
            IO.println("3. Top 3 Fraudes de Maior Valor: ");
            maioresSuspeitos.forEach(IO::println);


            //4. Prejuízo Total: 57393771.84
            BigDecimal prejuizoTotal =  lista.stream().filter(Transaction::isFraud).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            IO.println("4. Prejuízo Total: " + prejuizoTotal);

            //5. Fraudes por Tipo
            IO.println("5. Fraudes por Tipo");
            long totalFraudesPAYMENT = lista.stream().filter(t -> t.isFraud() &&  EnumTipoTransacao.PAYMENT.equals(t.getType())).count();
            long totalFraudesCASH_IN = lista.stream().filter(t -> t.isFraud() &&  EnumTipoTransacao.CASH_IN.equals(t.getType())).count();
            long totalFraudesCASH_OUT = lista.stream().filter(t -> t.isFraud() &&  EnumTipoTransacao.CASH_OUT.equals(t.getType())).count();
            long totalFraudesDEBIT = lista.stream().filter(t -> t.isFraud() &&  EnumTipoTransacao.DEBIT.equals(t.getType())).count();
            long totalFraudesTRANSFER = lista.stream().filter(t -> t.isFraud() &&  EnumTipoTransacao.TRANSFER.equals(t.getType())).count();

            IO.println("Total PAYMENT: " + totalFraudesPAYMENT);
            IO.println("Total CASH_IN: " + totalFraudesCASH_IN);
            IO.println("Total CASH_OUT: " + totalFraudesCASH_OUT);
            IO.println("Total DEBIT: " + totalFraudesDEBIT);
            IO.println("Total TRANSFER: " + totalFraudesTRANSFER);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Transaction processarLinha(String linha) {
        Transaction transaction = null;
        String[] columns = linha.split(",");
        try {
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
