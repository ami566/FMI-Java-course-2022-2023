import bg.sofia.uni.fmi.mjt.netflix.NetflixRecommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException {
        FileReader dataset = new FileReader("dataset.csv");
        BufferedReader reader = new BufferedReader(dataset);
        NetflixRecommender rec = new NetflixRecommender(reader);
        rec.getAllGenres().stream().forEach(el -> System.out.println(el));
    }
}
