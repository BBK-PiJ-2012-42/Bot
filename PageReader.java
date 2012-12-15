package spambot;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tnealo01
 */
public class PageReader implements WebPage {


    // Not implemented now returns as many links as it can find.
    //private static int maxLinks = 50;
    private String urlString;
    private URL url;
    // Current list of lines from the webpage.
    private List<String> pageLines = new ArrayList<>();
    static private int counter;
    
    
    public static int getCount() {
        return counter;
    }
    
    public PageReader(String urlString) {
        this.urlString = urlString;
        counter += 1;
        // Add http:// to urlString if it doesn't have it.
        if (urlString.startsWith("www")) {
            urlString = "http://" + urlString;
        } 
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PageReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    @Override
    public String getUrl() {
        return urlString;
    }
    
    @Override
    public Set<String> getLinks() {
        if (pageLines.isEmpty()) {
            pageLines = getPage();
        } 
        Set<String> myLinkSet = new HashSet<String>(getListLinks(pageLines));
        System.out.println();
        return myLinkSet;
    }
    
    @Override
    public Set<String> getEmails() {
        if (pageLines.isEmpty()) {
            pageLines = getPage();
        } 
        Set<String> myEmailSet = new HashSet<>(getListEmails(pageLines));
        return myEmailSet;
    }
    
    private List<String> getPage() {
        // Get the page and return a nice list of lines
        // for use by the link getter and email getter methods.
        List<String> lines = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            System.out.println(url.toString());
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            do {
                line = bufferedReader.readLine();
                //System.out.println(line);
                lines.add(line);
            } while (line != null);  
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
    private List<String> getListLinks(List<String> lines) {
        String result;
        List<String> links = new ArrayList<>();
        String linkRegex = "href=\"[^>]*\">";
        for(String each : lines) {
            try {
                Scanner myScanner = new Scanner(each);
                do {
                    result = myScanner.findInLine(linkRegex);
                    // Inefficent logical construction.
                    if (result != null) {
                        // Remove the beginning and end href=""
                        result = result.replaceFirst("href=\"", "");
                        result = result.replaceFirst("\">*", "");
                        // Remove anything left over after a space.
                        result = result.replaceFirst(" .*", "");
                        result = result.replaceFirst("&amp.*", "");
                        // appends http://domain to the links that just begin with /
                        if (result.startsWith("/")) {
                            //result = "http://" + url.getHost() + result;
                            result = url.getHost() + result;
                        }
                        if (result.startsWith("www")) {
                            result = "http://" + result;
                        } 
                        // When checks complete add the url to links.
                        if (result.startsWith("https://") || result.startsWith("http://")) {
                            links.add(result);
                            //System.out.println(result);
                        }
                    }
                } while(result != null);
            } catch (NullPointerException e) {
                return links;
               //e.printStackTrace(); 
            }
        }      
        return links;        
    }
    
    private List<String> getListEmails(List<String> lines) {
        List<String> emails = new ArrayList<>();
        String result;
        String mailRegex = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
        for(String each : lines) {
            try {
                Scanner myScanner = new Scanner(each);
                do {
                    result = myScanner.findInLine(mailRegex);
                    if (result != null) {
                        emails.add(result);
                        //System.out.println(result);
                    }
                } while(result != null);
            } catch (NullPointerException e) {
                return emails;
               //e.printStackTrace(); 
            }
        }  
        return emails;
    }
    
    
}

    

