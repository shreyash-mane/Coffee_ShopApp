package uk.ac.hw.group20.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
	
	private static final String FILE_PATH = "data/last_id.txt";
    private static AtomicInteger counter = new AtomicInteger(readLastId());
    
    public static String generateIdByItem(String itemName) {
        int newId = counter.incrementAndGet();
        saveLastId(newId);
        return itemName + newId;
    }
    
    private static void saveLastId(int id) {
    	File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new RuntimeException("last_id.txt not found in data folder.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the Id to text file due to: " + e.getMessage());
        }
    }
    
    private static int readLastId() {
    	File file = new File(FILE_PATH);

        if (!file.exists()) {
            throw new RuntimeException("last_id.txt not found in data folder.");
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
        	System.out.print("Failed to read the last save ID due to: " + e.getMessage());
            return 100;
        }
    }

}
