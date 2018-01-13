package br.com.yrsoares;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import br.com.yrsoares.analises.Analise;
import br.com.yrsoares.analises.Grafico;
import br.com.yrsoares.modelos.Cursos;

public class RecomendaCursos {

	public static DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	static double dadosFinal[][] = new double[5][8];

	public static void main(String[] args) throws IOException, TasteException {

		int qtdVizinhos = 5;
		int qtdCursos = 5;

		double contP1 = 0.0;
		double contP3 = 0.0;
		double contP5 = 0.0;
		double contP1em5 = 0.0;
		double contP3em5 = 0.0;
		double contrecall = 0.0;
		double contf1 = 0.0;
		//double contap5 = 0.0;

		ArrayList<Integer> listaCursosRecomendados = new ArrayList<Integer>();
		ArrayList<Integer> listaCursosUsuario = new ArrayList<Integer>();
		ArrayList<Integer> cursosVizinhos = new ArrayList<Integer>();
		ArrayList<Integer> resultadoCursos = new ArrayList<Integer>();
		ArrayList<Double> resultadoFinal = new ArrayList<Double>();

		// abre o arquivo de dados dos alunos (por area)
		File filearea = new File("src/main/resources/dados120porarea.csv");
		// cria o data model baseado nos dados dos alunos
		DataModel modelarea = new FileDataModel(filearea);
		
		// abre o arquivo de dados dos alunos (por curso)
		File filecursos = new File("src/main/resources/dados120porcurso.csv");
		// cria o data model baseado nos dados dos alunos
		DataModel modelcursos = new FileDataModel(filecursos);
		
		// abre o arquivo de dados dos alunos para similaridade
		File filesimilaridade = new File("src/main/resources/dadoscalculosimilaridade.csv");
		// cria o data model baseado nos dados dos alunos para similaridade
		DataModel modelsimilaridade = new FileDataModel(filesimilaridade);
				
				
		// instancia um objeto do tipo Similaridade para calcular a similaridade
		// de um determinado usuario com os outros usuarios
		Similaridade s = new Similaridade();

		int escolha = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Digite a Opcao:\n1-Gráfico\n2-Simulacao do Sistema\n3-Analise de Dados"));

		switch (escolha) {
		case 1:
			int escolhaGrafico = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Digite a Opcao:\n1-Gráfico Por Área\n2-Grafico por Curso\n3-Grafico por Conteudo\n4-Grafico Colaborativa"));
			
			for (int t = 0; t < 5; t++) {
				for (int h = 1; h < modelsimilaridade.getNumUsers(); h++) {
					
					if(escolhaGrafico==1) {
						resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelarea, modelsimilaridade, s,
								t+1, h, 1);
					}
					
					if(escolhaGrafico==2) {
						resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelcursos, modelsimilaridade, s,
								t+1, h, 2);
					}
					
					if(escolhaGrafico==3) {
						resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelcursos, modelsimilaridade, s,
								t+1, h, 3);
					}
					
					if(escolhaGrafico==4) {
						resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelcursos, modelsimilaridade, s,
								t+1, h, 4);
					}
					
					
					contP1 += resultadoFinal.get(0);
					contP3 += resultadoFinal.get(1);
					contP5 += resultadoFinal.get(2);
					contP1em5 += resultadoFinal.get(3);
					contP3em5 += resultadoFinal.get(4);
					contrecall += resultadoFinal.get(5);
					contf1 += resultadoFinal.get(6);
					//contap5 += resultadoFinal.get(7);

					listaCursosRecomendados = new ArrayList<Integer>();
					listaCursosUsuario = new ArrayList<Integer>();
					cursosVizinhos = new ArrayList<Integer>();
					resultadoCursos = new ArrayList<Integer>();
					resultadoFinal = new ArrayList<Double>();

				}

				dadosFinal[t][0] = contP1 / modelsimilaridade.getNumUsers();
				dadosFinal[t][1] = contP3 / modelsimilaridade.getNumUsers();
				dadosFinal[t][2] = contP5 / modelsimilaridade.getNumUsers();
				dadosFinal[t][3] = contP1em5 / modelsimilaridade.getNumUsers();
				dadosFinal[t][4] = contP3em5 / modelsimilaridade.getNumUsers();
				dadosFinal[t][5] = contrecall / modelsimilaridade.getNumUsers();
				dadosFinal[t][6] = contf1 / modelsimilaridade.getNumUsers();
				//dadosFinal[t][7] = contap5 / modelsimilaridade.getNumUsers();

				contP1 = 0;
				contP3 = 0;
				contP5 = 0;
				contP1em5 = 0;
				contP3em5 = 0;
				contrecall = 0;
				contf1 = 0;
				//contap5 = 0;
				listaCursosRecomendados = new ArrayList<Integer>();
				listaCursosUsuario = new ArrayList<Integer>();
				cursosVizinhos = new ArrayList<Integer>();
				resultadoCursos = new ArrayList<Integer>();
				resultadoFinal = new ArrayList<Double>();

			}
			final Grafico demo = new Grafico("Resultados", createDataset(), modelsimilaridade.getNumUsers());
			demo.pack();
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);

			break;
		case 2:
			int usuario = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o usuario: "));
			for (int t = 1; t < 6; t++) {
				resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelcursos, modelsimilaridade, s,
						t, usuario, 2);

				Cursos c = new Cursos();
				System.out.println("USUARIO: " + usuario);
				System.out.println("CURSOS USUARIO");
				for (int i = 0; i < listaCursosUsuario.size(); i++) {
					//System.out.println(listaCursosUsuario.get(i) + " - " + c.NomeCursos(listaCursosUsuario.get(i)));
					System.out.println(listaCursosUsuario.get(i));
				}

				System.out.println("\nCURSOS RECOMENDADOS");
				for (int i = 0; i < listaCursosRecomendados.size(); i++) {
					//System.out.println(listaCursosRecomendados.get(i) + " - " + c.NomeCursos(listaCursosRecomendados.get(i)));
					System.out.println(listaCursosRecomendados.get(i));
				}

				System.out.println("\nP@1 = " + resultadoFinal.get(0) + "\nP@3 = " + resultadoFinal.get(1) + "\nP@5 = "
						+ resultadoFinal.get(2) + "\nP@1 em 5 = " + resultadoFinal.get(3) + "\nP@3 em 5 = "
						+ resultadoFinal.get(4) + "\nRecall = " + resultadoFinal.get(5) + "\nF1 = "
						+ resultadoFinal.get(6)/* + "\nAP@5 = " + resultadoFinal.get(7)*/);
			listaCursosRecomendados = new ArrayList<Integer>();
			listaCursosUsuario = new ArrayList<Integer>();
			cursosVizinhos = new ArrayList<Integer>();
			resultadoCursos = new ArrayList<Integer>();
			resultadoFinal = new ArrayList<Double>();			
			}
			break;

		case 3:
			int tecnica = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Digite a Técnica à Utilizar:\n1-Pearson\n2-Spearman\n3-Tanimoto\n4-Euclidean\n5-City Block"));

			for (int h = 1; h < modelsimilaridade.getNumUsers(); h++) {
				resultadoFinal = calcula(qtdVizinhos, qtdCursos, listaCursosRecomendados, listaCursosUsuario, modelsimilaridade, modelsimilaridade, s,
						tecnica, h, 2);

				contP1 += resultadoFinal.get(0);
				contP3 += resultadoFinal.get(1);
				contP5 += resultadoFinal.get(2);
				contP1em5 += resultadoFinal.get(3);
				contP3em5 += resultadoFinal.get(4);
				contrecall += resultadoFinal.get(5);
				contf1 += resultadoFinal.get(6);
				//contap5 += resultadoFinal.get(7);

				listaCursosRecomendados = new ArrayList<Integer>();
				listaCursosUsuario = new ArrayList<Integer>();
				cursosVizinhos = new ArrayList<Integer>();
				resultadoCursos = new ArrayList<Integer>();
				resultadoFinal = new ArrayList<Double>();

			}

			System.out.println("QUANTIDADE DE ALUNOS NA BASE: " + modelsimilaridade.getNumUsers());
			if (tecnica == 1) {
				System.out.println("TÉCNICA UTILIZADA: COEFICIENTE DE PEARSON");
			}
			if (tecnica == 2) {
				System.out.println("TÉCNICA UTILIZADA: COEFICIENTE DE SPEARMAN");
			}

			if (tecnica == 3) {
				System.out.println("TÉCNICA UTILIZADA: COEFICIENTE DE TANIMOTO");
			}
			if (tecnica == 4) {
				System.out.println("TÉCNICA UTILIZADA: DISTÂNCIA EUCLIDIANA");
			}
			if (tecnica == 5) {
				System.out.println("TÉCNICA UTILIZADA: CITY BLOCK");
			}
			System.out.println("Médias: ");
			System.out.println("P@1: " + contP1 / modelsimilaridade.getNumUsers());
			System.out.println("P@3: " + contP3 / modelsimilaridade.getNumUsers());
			System.out.println("P@5: " + contP5 / modelsimilaridade.getNumUsers());
			System.out.println("P1EM5: " + contP1em5 / modelsimilaridade.getNumUsers());
			System.out.println("P3EM5 5: " + contP3em5 / modelsimilaridade.getNumUsers());
			System.out.println("RECALL: " + contrecall / modelsimilaridade.getNumUsers());
			System.out.println("F1: " + contf1 / modelsimilaridade.getNumUsers());
			//System.out.println("AP@5: " + contap5 / modelsimilaridade.getNumUsers());
			break;
		}

	}

	public static ArrayList<Double> calcula(int qtdVizinhos, int qtdCursos, ArrayList<Integer> listaCursosRecomendados,
			ArrayList<Integer> listaCursosUsuario, DataModel model, DataModel modelsimilaridade, Similaridade s, int tecnica, int us, int tipoGrafico)
			throws TasteException, FileNotFoundException {
		ArrayList<Integer> cursosVizinhos;
		ArrayList<Integer> resultadoCursos;
		ArrayList<Double> resultadoFinal;

		int idiniciocurso = 9;
		int idfimcurso = 14;

		// o array "cursosVizinhos" recebe os cursos dos "qtdVizinhos" vizinhos
		// mais
		// similares ao usuario
		cursosVizinhos = s.calculaSimilaridade(us, model, modelsimilaridade, qtdVizinhos, tecnica, tipoGrafico);

		// o array resultadoCursos recebe o "qtdCursos" recomendados para o
		// usuario
		FiltragemCursos fc = new FiltragemCursos();
		resultadoCursos = fc.filtraCursos(cursosVizinhos, qtdCursos, qtdVizinhos, tipoGrafico);

		// adiciona os id dos cursos que o usuario escolheu

		for (int q = idiniciocurso; q < idfimcurso; q++) {
			if (model.getPreferenceValue(us, q) != null) {
				Float f = new Float(model.getPreferenceValue(us, q));
				Integer i = new Integer(f.intValue());
				listaCursosUsuario.add(i);
			}
		}

		// adiciona os id dos cursos que foram recomendados
		for (int i = 0; i < resultadoCursos.size(); i++) {
			listaCursosRecomendados.add(resultadoCursos.get(i));
		}
		Analise a = new Analise();
		resultadoFinal = a.analiseDados(listaCursosUsuario, listaCursosRecomendados, us);
		return resultadoFinal;
	}

	public static CategoryDataset createDataset() {

		//final String pearson = "Pearson";
		final String spearman = "Spearman";
		//final String tanimoto = "Tanimoto";
		//final String euclidiana = "Euclidiana";
		//final String cityblock = "City Block";

		// column keys...
		final String categoria1 = "P@1";
		final String categoria2 = "P@3";
		final String categoria3 = "P@5";
		final String categoria4 = "P1em5";
		final String categoria5 = "P3em5";
		final String categoria6 = "Recall";
		final String categoria7 = "F1";
		final String categoria8 = "AP@5";
/*
		dataset.addValue(dadosFinal[0][0], pearson, categoria1);
		dataset.addValue(dadosFinal[0][1], pearson, categoria2);
		dataset.addValue(dadosFinal[0][2], pearson, categoria3);
		dataset.addValue(dadosFinal[0][3], pearson, categoria4);
		dataset.addValue(dadosFinal[0][4], pearson, categoria5);
		dataset.addValue(dadosFinal[0][5], pearson, categoria6);
		dataset.addValue(dadosFinal[0][6], pearson, categoria7);
		//dataset.addValue(dadosFinal[0][7], pearson, categoria8);
*/
		dataset.addValue(dadosFinal[1][0], spearman, categoria1);
		dataset.addValue(dadosFinal[1][1], spearman, categoria2);
		dataset.addValue(dadosFinal[1][2], spearman, categoria3);
		dataset.addValue(dadosFinal[1][3], spearman, categoria4);
		dataset.addValue(dadosFinal[1][4], spearman, categoria5);
		dataset.addValue(dadosFinal[1][5], spearman, categoria6);
		dataset.addValue(dadosFinal[1][6], spearman, categoria7);
		//dataset.addValue(dadosFinal[1][7], spearman, categoria8);
		
		/*
		dataset.addValue(dadosFinal[2][0], tanimoto, categoria1);
		dataset.addValue(dadosFinal[2][1], tanimoto, categoria2);
		dataset.addValue(dadosFinal[2][2], tanimoto, categoria3);
		dataset.addValue(dadosFinal[2][3], tanimoto, categoria4);
		dataset.addValue(dadosFinal[2][4], tanimoto, categoria5);
		dataset.addValue(dadosFinal[2][5], tanimoto, categoria6);
		dataset.addValue(dadosFinal[2][6], tanimoto, categoria7);
		//dataset.addValue(dadosFinal[2][7], tanimoto, categoria8);
		 */
/*		dataset.addValue(dadosFinal[3][0], euclidiana, categoria1);
		dataset.addValue(dadosFinal[3][1], euclidiana, categoria2);
		dataset.addValue(dadosFinal[3][2], euclidiana, categoria3);
		dataset.addValue(dadosFinal[3][3], euclidiana, categoria4);
		dataset.addValue(dadosFinal[3][4], euclidiana, categoria5);
		dataset.addValue(dadosFinal[3][5], euclidiana, categoria6);
		dataset.addValue(dadosFinal[3][6], euclidiana, categoria7);
		//dataset.addValue(dadosFinal[3][7], euclidian, categoria8);

		dataset.addValue(dadosFinal[4][0], cityblock, categoria1);
		dataset.addValue(dadosFinal[4][1], cityblock, categoria2);
		dataset.addValue(dadosFinal[4][2], cityblock, categoria3);
		dataset.addValue(dadosFinal[4][3], cityblock, categoria4);
		dataset.addValue(dadosFinal[4][4], cityblock, categoria5);
		dataset.addValue(dadosFinal[4][5], cityblock, categoria6);
		dataset.addValue(dadosFinal[4][6], cityblock, categoria7);
		//dataset.addValue(dadosFinal[4][7], cityblock, categoria8);
*/
		return dataset;

	}
}