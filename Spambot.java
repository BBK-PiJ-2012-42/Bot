/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spambot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author annataylor
 */
public class Spambot {
    private String seed;
    private int threadCount;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Spambot spambot = new Spambot();
        spambot.launch();
        
    }
    
    private void launch() {
        System.out.println("**SPAMBOT**");
        System.out.print("Please enter a seed URL: ");
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            seed = reader.readLine();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.print("Enter the number of crawlers to release: ");
        try {
            threadCount = Integer.parseInt(reader.readLine());           
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        Crawler crawler = new Crawler(seed, threadCount);
        crawler.launch();
        

   
    }
}
