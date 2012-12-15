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
public class SpambotMulti {
    private String seed;
    private int threadCount;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SpambotMulti spambot = new SpambotMulti();
        spambot.launch();
        
    }
    
    private void launch() {
        System.out.println("**SPAMBOT-MULTI**");
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
        Thread thread0 = new Thread(new MultiCrawler(seed, 0));
        thread0.start();
        if (threadCount > 1) {
            for (int i = 1; i < threadCount; i++) {
                    Thread thread = new Thread(new MultiCrawler(i));
                    try {
                        thread.sleep(3000);//so that initial thread can build up links set
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Thread " + i + " initialising");
                    thread.start();
            }
        }
             
        

   
    }
    
    

   
}
