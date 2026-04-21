package br.com.zenon.fraud;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {
    static void main() {
        try {
            lerArquivo("data/PS_20174392719_1491204439457_log.csv", 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void lerArquivo(String arquivo, Integer totalLinhasEsperadas) throws Exception {
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
        for (int i = 0; i < 10; i++) {
            IO.println(transactions.get(i));
        }


        System.out.println("Tempo: " + ((System.nanoTime() - inicio) / 1_000_000) + " ms");
    }

    private static Transaction processarLinha(String linha) {
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
