public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words){
        String prefix = "";
        if(words == null || words.length == 0) return prefix;
        if(words.length == 1) return words[0];

        for (int i = 0; i < words[0].length() ; i++){
            int correct = 0;
            int j = 0;
            for (j = 1; j < words.length; j++) {
                if(i > words[j].length() - 1) break;
                if (Character.compare(words[j].charAt(i), words[0].charAt(i)) == 0)
                   correct++;
            }
            if (prefix.length() != i || i > words[j-1].length() - 1) break;
            if(correct == words.length - 1)
                prefix += words[0].charAt(i);
        }
        return prefix;
    }

    public static void main(String[] args) {
        System.out.println(getLongestCommonPrefix(new String[]{"flo", "flow", "flowi"}));
    }
}
