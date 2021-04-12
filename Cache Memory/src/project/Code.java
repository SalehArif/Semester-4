package project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Code {

	public static void main(String[] args) {
		FileWriter writer;
		Random rand = new Random();
		//int b= 21474836;
		int max = 214748367;
		int min = 210111000;
		//int randomNum = rand.nextInt((max - min) + 1) + min;
		
		try {
			writer = new FileWriter("addresses1.txt");
			BufferedWriter buffer = new BufferedWriter(writer);  
			for(int i=0;i<100000;i++) {
			buffer.write(String.format("%08x", (rand.nextInt((max - min) + 1) + min))+"\r\n");
			}
		    buffer.close();
		    System.out.println("Success");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		/*
		try(FileWriter fw = new FileWriter("filename.txt",true);){
			fw.write("\r\n"); //line separator
			fw.write("any string");
		}catch(IOException e) {
		System.out.println("Aisa ni hona chahiye");
		}*/
	}

	//random number generation 
	//int randomNum = ThreadLocalRandom.current().nextInt(0, b);
	

}
