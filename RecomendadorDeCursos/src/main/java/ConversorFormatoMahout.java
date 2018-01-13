import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConversorFormatoMahout {

	public static void main(String[] args) throws IOException {

		// cria o arquivo novo e cria o arquivo para escrever
		FileWriter arq = new FileWriter("src/main/resources/dados120porcurso.csv");
		PrintWriter gravarArq = new PrintWriter(arq);

		// ler o arquivo csv
		Scanner in = new Scanner(new FileReader("dadosgeral120porcurso.csv"));
		while (in.hasNextLine()) {
			// le a primeira linha
			String line = in.nextLine();
			// copia o valor da primeira linha para uma string temporaria
			String pedaco = line;
			// quebra a string temporaria em varias substrings com o separador ;
			String split[] = pedaco.split(";");

			for (int i = 1; i < split.length; i++) {
				// grava no arquivo novo no formato do mahout
				// (UserId,ItemId,Rating)
				gravarArq.printf(split[0] + "," + i + "," + split[i] + "%n");
			}
		}
		arq.close();
		in.close();
	}
}
