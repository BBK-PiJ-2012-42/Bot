package spambot;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author annataylor
 */
public class Crawler implements Runnable {
	private Set<String> links = new HashSet<String>();
	private Set<String> visitedLinks = new HashSet<String>();
	private Set<String> emails = new HashSet<String>();
	private Set<String> subLinkSet = null; //for testing purposes
	private int threadCount = 0;
        private String element;
        private boolean emailsPrinted = false;
        
        
	
	public Crawler(String url, int threadCount) { //adds seed website upon construction of crawler.
		this.threadCount = threadCount;
                links.add(url);
        }       
	
	public Crawler() { 
		super();
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
	
	private synchronized void addLinks(Set<String> linkSubSet) {
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
	
	
	private void addEmails(Set<String> subEmailSet) {
            emails.addAll(subEmailSet);
	}
	
	private void addToVisitedSet(String url) {
            visitedLinks.add(url);
		
	}
	
	private synchronized void printVisited() {
		Iterator itr = visitedLinks.iterator();
		System.out.println("VISITED");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	private synchronized void printEmails() {
		Iterator itr = emails.iterator();
		System.out.println("EMAILS");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	private synchronized void printLinks() {
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
	
	public void launch() {  
            
           for (int i = 0; i < threadCount; i++) {
              String id = "Thread " + i;
              Thread thread = new Thread(this);
               System.out.println("Thread " + i + " initialising...");
               //System.out.println(thread.getId());
               thread.start();                
            }         
            
        }
        
                 
        
        
        @Override
        public void run() {
            

            /**
            * For each link in the main set, invokes a PageReader to visit the link and parse the page,
            * while the set of links isn't empty and the number of sites visited is less than 51.
            */
          
            Iterator<String> iterator = links.iterator(); //at this point iterator only has bbk.ac.uk, hence it gets reassigned at end of loop to include updated links.
        
            while (iterator.hasNext() && PageReader.getCount() <= 50) {
                System.out.println("1. Entering while"); //used to see thread behaviour
		element = iterator.next();
                System.out.println("Scanning... " + element);               
		PageReader pageReader = new PageReader(element);               
		addEmails(pageReader.getEmails());                
		addLinks(pageReader.getLinks());
		addToVisitedSet(element);
//		this.printEmails();
//		this.printLinks();
//		this.printVisited();
		links.remove(element);
		iterator = links.iterator();
                System.out.println("2. Finishing while");//used to see thread behaviour
		}
            if (!emailsPrinted) { //ensures emails are printed only once
                this.printEmails(); 
                emailsPrinted = true;
            }
                
		
		/**
		Iterator<String> iterator = Bot.getLinkSet().iterator(); //at this point iterator only has bbk.ac.uk, hence it gets reassigned at end of loop to include updated links.
		while (iterator.hasNext() && TestReader.getCount() <= 50) {		
			String element = iterator.next();
			//System.out.println(element); //test print
			TestReader testReader = new TestReader(element);			
			Bot.addEmails(testReader.getEmails()); 
			Bot.addLinks(testReader.getLinks());
			Bot.addToVisitedSet(element);
			Bot.getLinkSet().remove(element);
			iterator = Bot.getLinkSet().iterator();
		}
		*/
		
		
		
	
		
	
	
	
		
		
			
	}
}

/**
* Add threading -- this class will implement Runnable
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
    

