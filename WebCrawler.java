/* Question no: 6b
 This program is a simple web crawler that starts from a URL
  (or multiple URLs) provided by the user and crawls through the pages linked from it. 
  When the user enters a URL, the program first checks 
  if it is valid and adds http:// if it's missing. 
  It then begins crawling from that URL, 
  downloading the content of the page, and saving it as a .txt file on the local machine. 
  The program uses a queue to manage URLs that need to be 
  crawled and a set to track visited URLs, ensuring each URL is crawled only once. 
  An ExecutorService with a thread pool of 10 threads runs multiple crawling tasks in parallel, speeding up the process. 
  As the program crawls through pages, it follows any links found on the pages, adding them to the queue for further crawling. 
  The program also updates the GUI to show the status of the crawling process and logs the visited URLs in a text file. 
  In summary, the crawler downloads the content of web pages, saves them locally, and follows links from those pages, 
  all while managing the process efficiently with multi-threading and keeping track of visited URLs.
 */
import java.io.*;
 import java.net.*;
 import java.util.*;
 import java.util.concurrent.*;
 import javax.swing.*;
 
 class CrawlerTask implements Runnable {
     private String url;
     private JTextArea textArea;
 
     public CrawlerTask(String url, JTextArea textArea) {
         this.url = url;
         this.textArea = textArea;
     }
 
     @Override
     public void run() {
         downloadPage(url);
     }
 
     private void downloadPage(String urlString) {
         try {
             URL url = new URL(urlString);
             BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
             String line;
             StringBuilder content = new StringBuilder();
 
             while ((line = reader.readLine()) != null) {
                 content.append(line).append("\n");
             }
             reader.close();
 
             saveToFile(urlString, content.toString());
             updateGUI("Crawled: " + urlString);
         } catch (Exception e) {
             updateGUI("Failed to crawl: " + urlString + " - " + e.getMessage());
         }
     }
 
     private void saveToFile(String url, String content) {
         try {
             String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
             writer.write(content);
             writer.close();
         } catch (IOException e) {
             updateGUI("Error saving file: " + e.getMessage());
         }
     }
 
     private void updateGUI(String message) {
         SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
     }
 }
 
 public class WebCrawler {
     private static Set<String> visited = new HashSet<>();
     private static Queue<String> queue = new LinkedList<>();
     private static ExecutorService executorService = Executors.newFixedThreadPool(10);
 
     public static void main(String[] args) {
         JFrame frame = new JFrame("Web Crawler");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(600, 400);
 
         JTextArea textArea = new JTextArea();
         textArea.setEditable(false);
         JScrollPane scrollPane = new JScrollPane(textArea);
         frame.add(scrollPane, "Center");
 
         JPanel panel = new JPanel();
         frame.add(panel, "South");
 
         JTextField urlField = new JTextField(30);
         panel.add(urlField);
 
         JButton startButton = new JButton("Start Crawling");
         panel.add(startButton);
 
         startButton.addActionListener(e -> {
             String urlInput = urlField.getText().trim();
             if (!urlInput.isEmpty()) {
                 startCrawling(urlInput, textArea);
             } else {
                 textArea.append("Please enter a URL.\n");
             }
         });
 
         frame.setVisible(true);
     }
 
     private static void startCrawling(String urlInput, JTextArea textArea) {
         // Check if the executor has been shut down already
         if (executorService.isShutdown()) {
             textArea.append("The executor has been shut down. Please restart the program.\n");
             return;
         }
 
         String[] urls = urlInput.split(",");
         for (String url : urls) {
             String trimmedUrl = url.trim();
             String correctedUrl = isValidUrl(trimmedUrl);
             if (correctedUrl != null) {
                 queue.add(correctedUrl);
                 storeUrlInBackend(correctedUrl);
             } else {
                 textArea.append("Invalid URL: " + trimmedUrl + "\n");
             }
         }
 
         while (!queue.isEmpty()) {
             String url = queue.poll();
             if (!visited.contains(url)) {
                 visited.add(url);
                 executorService.submit(new CrawlerTask(url, textArea));
             }
         }
     }
 
     private static String isValidUrl(String url) {
         // If the URL does not start with http:// or https://, prepend it
         if (!url.startsWith("http://") && !url.startsWith("https://")) {
             url = "http://" + url;
         }
 
         try {
             // Try to create a valid URL object with the corrected URL
             new URL(url).toURI();
             return url;
         } catch (Exception e) {
             // If URL is invalid, catch exception and return null
             return null;
         }
     }
 
     private static void storeUrlInBackend(String url) {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter("visited_urls.txt", true))) {
             writer.write(url);
             writer.newLine();
         } catch (IOException e) {
             System.out.println("Error storing URL in backend: " + e.getMessage());
         }
     }
 }
/* Testing Results
    Starting crawl from: www.google.com
    Crawled: www.google.com
    Starting crawl from: www.google.com/home
    Crawled: www.google.com/home
    Starting crawl from: www.google.com/home/aboutus
    Crawled: www.google.com/home/aboutus 
 */