package spambot;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

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
    private String url;
    // Current list of lines from the webpage.
    private List<String> pageLines = new ArrayList<String>();
    static private int counter;
    
    
    public static int getCount() {
        return counter;
    }
    
    public PageReader(String url) {
        this.url = url;
        counter += 1;
    }
            
    @Override
    public String getUrl() {
        return url;
    }
    
    @Override
    public Set<String> getLinks() {
        if (pageLines.isEmpty()) {
            pageLines = getPage();
        } 
        Set<String> myLinkSet = new HashSet<String>(getListLinks(pageLines));
        ///////
        System.out.println();
        //System.out.println("Gathered emails");
//        Iterator<String> emailIterator = myLinkSet.iterator();
//        while (emailIterator.hasNext()) {
//            System.out.println("Checking:::: " + emailIterator.next());
//        }
        return myLinkSet;
    }
    
    @Override
    public Set<String> getEmails() {
        if (pageLines.isEmpty()) {
            pageLines = getPage();
        } 
        Set<String> myEmailSet = new HashSet<String>(getListEmails(pageLines));
        return myEmailSet;
    }
    
    private List<String> getPage() {
        // Get the page and return a nice list of lines
        // for use by the link getter and email getter methods.
        List<String> lines = new ArrayList<String>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
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
        List<String> links = new ArrayList<String>();
        String linkRegex = "href=\"[^>]*\">";
        for(String each : lines) {
            try {
                Scanner myScanner = new Scanner(each);
                do {
                    result = myScanner.findInLine(linkRegex);
                    if (result != null) {
                        // Remove the beginning and end href=""
                        result = result.replaceFirst("href=\"", "");
                        result = result.replaceFirst("\">*", "");
                        // Remove anything left over after a space.
                        result = result.replaceFirst(" .*", "");
                        result = result.replaceFirst("&amp.*", "");
                        // Still does not append http://domain to the links without it or
                        // pass over emailto: and javascript: links.
                        if (result.startsWith("https://") || result.startsWith("http://") || result.startsWith("www.")) {
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
        List<String> emails = new ArrayList<String>();
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

    

