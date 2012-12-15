/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spambot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author annataylor
 */
public class MultiCrawler implements Runnable {

	private static Set<String> links = new HashSet<String>();
	private static Set<String> visitedLinks = new HashSet<String>();
	private static Set<String> emails = new HashSet<String>();
	private Set<String> subLinkSet = null; //for testing purposes
        private String element;
        private static boolean emailsPrinted = false;
        private int threadID;
        
        
	
	public MultiCrawler(String url, int threadID) { //adds seed website upon construction of crawler.
            this.threadID = threadID;  
            links.add(url);
        }       
	
	public MultiCrawler(int threadID) { 
		this.threadID = threadID;
	}
	
	
	public Set<String> getLinks() {
		return links;
	}
	
	public Set<String> getVisited() {
		return visitedLinks;
	}
	
	public Set<String> getEmails() {
		return emails;
	}	
	
	private static synchronized void addLinks(Set<String> linkSubSet) {
                Set<String> duplicates = new HashSet<String>();
                String item;
		if (!visitedLinks.isEmpty()) {
                        //System.out.println("Checking visited...");
			Iterator<String> itr = linkSubSet.iterator();
			while (itr.hasNext()) {
                                item = itr.next();
				if (visitedLinks.contains(item)) {
                                        duplicates.add(item);
				}
			}
		}
                linkSubSet.removeAll(duplicates);
		links.addAll(linkSubSet);
	}
	
	
	private static void addEmails(Set<String> subEmailSet) {
            emails.addAll(subEmailSet);
	}
	
	private static void addToVisitedSet(String url) {
            visitedLinks.add(url);
		
	}
	
	private void printVisited() {
		Iterator itr = visitedLinks.iterator();
		System.out.println("VISITED");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	private void printEmails() {
		Iterator itr = emails.iterator();
		System.out.println("EMAILS");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	private void printLinks() {
		Iterator itr = links.iterator();
		System.out.println("LINKS");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
					
			
	
				
	/**
	public static void main(String[] args) {
	
		Crawler crawler = new Crawler();
		crawler.launch();
	}
	*/
	
	
        
                 
        
        
        @Override
        public void run() {
                   
            
            while (PageReader.getCount() <=50) { //links is empty when next thread comes, without thraed waiting
                element = this.selectFromSet();
                if (element == null) {
                    break;
                }
                System.out.println("Thread " + threadID + " scanning..." + element);
                PageReader pageReader = new PageReader(element);
                addEmails(pageReader.getEmails());
                addLinks(pageReader.getLinks());
            }
            if (!emailsPrinted) { //emails printed only once
                printEmails();
                emailsPrinted = true;
            } 
            
            
                
		
	}
        
        private synchronized String selectFromSet() {
            Iterator<String> iterator = links.iterator();
            String item;
            if (iterator.hasNext()) {
                item = iterator.next();
                links.remove(item);
                addToVisitedSet(item);
                return item;
                }
            return null;            
        }
}

/**
* Split iteration off into a synchronized method that returns a String
* and delete item from central set straight away to avoid other threads looking
* to use it and turning it away in add method -- check that they're doing this
* looks like they scan it (as the println is earlier) but don't actually do anything 
* the whole time.
* Deal with unchecked exception handling.
*/







/**
First, they need to know / remember all the links to be visited;
they also need to know / remember those links that have already been visited,
so as to not visit and parse the same page twice. 
The third type of data they need to store are the emails they get from webpages.
Crawlers need a way to terminate their execution.
It is up to you to decide when crawlers will
end. Some possibilities for your considerations are time limits, number of email addresses found,
number of unsuccessful attempts to read a link, or to find new links.
In a first stage of design, it may help to have only one active crawler (i.e. one thread). Once the
functionality is ready, you should be able to span several crawlers in parallel, all of them sharing the
information described above. You should at least launch a crawler per processor in your machine2,
and possibly more, because I/O waits over the network connection will make your crawlers waste
a lot of time.
*/
    


    

