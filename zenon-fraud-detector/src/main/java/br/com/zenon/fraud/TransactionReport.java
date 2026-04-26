package br.com.zenon.fraud;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TransactionReport {
     void main() {
         try {
             String path = "data/PS_20174392719_1491204439457_log.csv";
             //List<Transaction> listTransactions = lerArquivo(path, null);

             long tInicio = System.currentTimeMillis();
             List<String> linhasArquivo = Files.readAllLines(Path.of(path), Charset.defaultCharset());

             List<Transaction> listTransactions =linhasArquivo.stream()
                     .skip(1)
                     .map(this::montarTransaction)
                     .toList();


             long tFim = System.currentTimeMillis();
             long totalDeFraudes = listTransactions.stream().filter(Transaction::isFraud).count();
             BigDecimal total = listTransactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

             NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt","BR"));

             IO.println("Total de dados: " + listTransactions.size());
             IO.println("Total de fraudes: " + totalDeFraudes);
             IO.println("Valor total: " + nf.format(total));
             IO.println("Tempo de carregamento de dados: " + (tFim- tInicio));


         } catch (Exception e) {
             throw new RuntimeException(e);
         }
     }

     private Transaction montarTransaction(String linha) {
         return processarLinha(linha);
     }

    private List<Transaction> lerArquivo(String arquivo, Integer totalLinhasEsperadas) throws Exception {
        long inicio = System.nanoTime();

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
        List<Transaction> transactions = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(arquivo, "r");
             FileChannel fc = raf.getChannel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
            CharBuffer charBuffer = CharBuffer.allocate(8192);

            StringBuilder linhaAtual = new StringBuilder();
            int numLinha = 0;

            while (fc.read(byteBuffer) != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();

                while (charBuffer.hasRemaining()) {
                    char c = charBuffer.get();

                    if (c == '\n') {
                        //processarLinha(linhaAtual.toString(), numLinha++);
                        if (numLinha > 0) {
                            transactions.add(processarLinha(linhaAtual.toString()));
                        }
                        linhaAtual.setLength(0);
                        numLinha++;
                    } else if (c != '\r') {
                        linhaAtual.append(c);
                    }
                }

                byteBuffer.compact();
                charBuffer.clear();
            }

            // última linha
            if (!linhaAtual.isEmpty()) {
                //processarLinha(linhaAtual.toString(), numLinha);
                transactions.add(processarLinha(linhaAtual.toString()));
            }
        }

        IO.println("Quantidde Transactions: " + transactions.size());
        System.out.println("Tempo: " + ((System.nanoTime() - inicio) / 1_000_000) + " ms");

        return transactions;
    }

    private Transaction processarLinha(String linha) {
        Transaction transaction = null;
        String[] columns = linha.split(",");
        transaction = new Transaction(Integer.valueOf(columns[0]), //
                EnumTipoTransacao.getTipoTransacao(columns[1]),//
                new BigDecimal(columns[2]),//
                new Customer(columns[3], new BigDecimal(columns[4]), new BigDecimal(columns[5])),//
                new ClienteDestino(columns[6], new BigDecimal(columns[7]), new BigDecimal(columns[8])),//
                "1".equals(columns[9]),//
                "1".equals(columns[10]));
        return transaction;
    }
}
