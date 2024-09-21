/*
 * Name: Sairam Soundararajan
 * Date: 11-13-22
 * Course: CMSC451: Design and Analysis of Computer Algorithms
 * Instructor: Prof. Dennis Didulo
 * Institution: University of Maryland Global Campus
 *
 * Purpose: This program allows the user to pick an output file in order to display the results/report of an arbitrary iterative/recursive sort
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Report {
	private JFrame frame;
	private JPanel panel;
	private JButton selectFile_button, displayTable_button;
	private JTable report_table;

	private File selectedFile;

	public Report() {
		displayMainGUI();
	}

	public void displayMainGUI() {
		frame = new JFrame();
		frame.setTitle("Report");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);

		selectFile_button = new JButton("SELECT FILE");
		selectFile_button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectFile();
			}
		});

		displayTable_button = new JButton("DISPLAY TABLE");
		displayTable_button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				displayTable();
			}
		});

		panel = new JPanel();
		panel.add(selectFile_button);

		panel.add(displayTable_button);


		frame.add(panel);

		frame.setVisible(true);
	}

	public void selectFile() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.showSaveDialog(null);
		// FileNameExtensionFilter filter = new FileNameExtensionFilter(
		// "txt");
		// fileChooser.setFileFilter(filter);
		selectedFile = fileChooser.getSelectedFile();
		System.out.println(selectedFile.getName());
		if (!(selectedFile.getName().length() > 3
				&& selectedFile.getName().substring(selectedFile.getName().length() - 3).equals("txt"))) {
			JOptionPane.showMessageDialog(null, "File is not of .txt");
			selectedFile = null;
		}
	}

	public void displayTable() {
		if (selectedFile == null) {
			JOptionPane.showMessageDialog(null, "No .txt file loaded");
			return;
		}

		JFrame table_frame = new JFrame("Report Table");
		JTable table = new JTable(
				new DefaultTableModel(new Object[] { "Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time" }, 0));

		getDataFromFile(table);
		table.setBounds(30, 40, 200, 300);
		JScrollPane sp = new JScrollPane(table);
		table_frame.add(sp);
		table_frame.setSize(500, 200);
		table_frame.setVisible(true);

	}

	private void getDataFromFile(JTable table) {
		Scanner fileReader;
		String[] data;

		try {
			fileReader = new Scanner(selectedFile);
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			while (fileReader.hasNextLine()) {
				int countSum = 0, timeSum = 0;
				double avgCount, avgTime, coefTime, size, coefCount;
				data = fileReader.nextLine().split(",");
				//System.out.println(data.length);
				size = Integer.parseInt(data[0]);

				double[] coefCountArr = new double[(data.length-1) / 2];
				double[] coefTimeArr = new double[(data.length-1) / 2];

				for (int i = 1; i < data.length; i += 2) {
					int d = Integer.parseInt(data[i]);
					// calc avg
					countSum += d;

					coefCountArr[i / 2] = d;
				}

				for (int i = 2; i < data.length; i += 2) {
					int d = Integer.parseInt(data[i]);
					timeSum += d;
					//System.out.println(i + ":" + i/2 + "," + d + "\n");
					coefTimeArr[(i / 2)-1] = d;
				}

				avgCount = (double) countSum / ((data.length - 1) / 2);
				avgTime = (double) timeSum / ((data.length - 1) / 2);
				coefTime = calcCoeOfVar(coefTimeArr, avgTime);
				coefCount = calcCoeOfVar(coefCountArr, avgCount);
				
				model.addRow(new Object[]{ size, avgCount, coefCount,avgTime,coefTime });
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double calcCoeOfVar(double[] coefArr, double mean) {

		// Calculate Standard Deviation
		double stdDevSum = 0;
		for (int i = 0; i < coefArr.length; i++) {
			stdDevSum += (coefArr[i] - mean) * (coefArr[i] - mean);
		}
		double stdDev = Math.sqrt(stdDevSum / (coefArr.length - 1));
		
		
		return stdDev / mean;
	}

	public static void main(String[] args) {
		new Report();
	} // main
} // Report
