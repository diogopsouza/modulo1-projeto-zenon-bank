package br.com.zenon.fraud;

import br.com.zenon.fraud.interfaces.TransactionRepositoryImpl;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Benchmark {

    private TransactionRepositoryImpl transactionListRepository;

    void main() {

        try {
            long tInicio = System.currentTimeMillis();
            List<Transaction> listTransactions = lerArquivo("data/PS_20174392719_1491204439457_log.csv", 100000);
            IO.println("Tempo de carregamento de dados: " + (System.currentTimeMillis() - tInicio));

            transactionListRepository = new TransactionRepositoryImpl(listTransactions);

            //realize uma busca pelo nome da origem (nameOrig) da última transação da lista (C1868032458), o pior caso, e meça o tempo de busca usando System.nanoTime()
            IO.println("Consultar transação C1868032458");
            buscarTransacaoPorOrigem("C1868032458");

            //erro
            IO.println("Consultar transação nao existente");
            buscarTransacaoPorOrigem("C12345");


        } catch (Exception e) {
            throw new RuntimeException("Erro ao transformar arquivo", e);
        }

    }

    private void buscarTransacaoPorOrigem(String nameOrig) {
        long tInicio = System.currentTimeMillis();
        try {
            Optional<Transaction> optTransacao = this.transactionListRepository.findByNameOrig(nameOrig);
            IO.println("Fim da busca. Tempo execução: " + (System.currentTimeMillis() - tInicio));
            if (optTransacao.isPresent()) {
                IO.println("Transacao: " + optTransacao.get().toString());
            } else {
                IO.println("Transacao " + nameOrig + " não encontrada");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        IO.println("------------------------------------------------------");
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
