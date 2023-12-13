package bg.sofia.uni.fmi.mjt.news.client;

import bg.sofia.uni.fmi.mjt.news.Article;
import bg.sofia.uni.fmi.mjt.news.SearchWords;
import bg.sofia.uni.fmi.mjt.news.server.ServerRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {
    private Scanner sc;
    private ServerRequest serverRequest;
    private SearchWords searchWords;
    private final String inputMenu = "Choose search type:\n " +
            "\t1 - by keywords only\n" +
            "\t2 - by keywords and country\n" +
            "\t3 - by keywords and category\n" +
            "\t4 - by all\n" +
            "Type 0 to exit\n";
    private final String enterKeyWords = "Enter keywords separated by space:\n ";
    private final String invalidCommand = "Invalid command!\n";
    private final String enterCountry = "Enter country:\n ";
    private final String inputSymbol = "> ";
    private final String enterCategory = "Enter category:\n ";
    private List<Article> articles;

    public List<Article> Articles() {
        return List.copyOf(articles);
    }

    public void setSearchWords(SearchWords sw) {
        searchWords = sw;
    }
    public ClientInterface(ServerRequest sr) throws IOException, InterruptedException {
        sc = new Scanner(System.in);
        serverRequest = sr;
        articles = new ArrayList<>();

    }

    public void start() throws IOException, InterruptedException {
        execute();
    }
    public boolean handleCommand(String command) {
        switch (command) {
            case "1" -> {
                System.out.print(enterKeyWords + inputSymbol);
                searchWords.setKeywords(sc.nextLine());
            }
            case "2" -> {
                System.out.print(enterKeyWords + inputSymbol);
                searchWords.setKeywords(sc.nextLine());
                System.out.print(enterCountry + inputSymbol);
                searchWords.setCountry(sc.nextLine());
            }
            case "3" -> {
                System.out.print(enterKeyWords);
                searchWords.setKeywords(sc.nextLine());
                System.out.print(enterCategory);
                searchWords.setCategory(sc.nextLine());
            }
            case "4" -> {
                System.out.print(enterKeyWords + inputSymbol);
                searchWords.setKeywords(sc.nextLine());
                System.out.print(enterCountry + inputSymbol);
                searchWords.setCountry(sc.nextLine());
                System.out.print(enterCategory + inputSymbol);
                searchWords.setCategory(sc.nextLine());
            }
            default -> {
                System.out.println(invalidCommand);
                return false;
            }
        }
        return true;
    }

    public void execute() throws IOException, InterruptedException {
        String command;
        while (true) {
            searchWords = new SearchWords();
            System.out.println(inputMenu);
            System.out.print(inputSymbol);
            command = sc.nextLine();
            if (command.equals("0")) {
                return;
            }
            if (!handleCommand(command)) {
                continue;
            }
            displayArticles();
        }
    }

    public void loadArticles(int page) throws IOException, InterruptedException {
        articles = serverRequest.getArticles(searchWords, page);
    }

    public void displayArticles() throws IOException, InterruptedException {
        int currPage = 1;
        int i = 1;
        boolean isDisplayed = false;
        String command;
        while (true) {
            if (!isDisplayed) {
                loadArticles(currPage);
                if (articles == null || articles.isEmpty()) {
                    System.out.println("No articles were found\n");
                    return;
                }

                for (Article a : articles) {
                    System.out.println("Article #" + i++ + System.lineSeparator());
                    a.print();
                    System.out.println("\n********************************************************************************\n");
                }
                System.out.println("--- Page " + currPage + " ---");
                isDisplayed = true;
            }

            String s = "Type:\n\t0 - exit; 1 - next page; 2 - previous page";

            System.out.println(s);
            System.out.print(inputSymbol);
            command = sc.nextLine();
            switch (command) {
                case "1" -> {
                    currPage++;
                    isDisplayed = false;
                }
                case "2" -> {
                    if (currPage == 1) {
                        System.out.println("There are no previous pages!\n");
                    } else {
                        currPage--;
                        isDisplayed = false;
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println(invalidCommand);
            }
        }
    }

}
