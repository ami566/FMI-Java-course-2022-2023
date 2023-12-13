public class DataCenter {
    public static int getCommunicatingServersCount(int[][] map)
    {
        int count = 0;
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if(map[i][j] == 1) {
                    boolean exists = false;
                        for (int k = 0; k < map[i].length; k++){
                            if(map[i][k] == 1 && k != j) {
                                exists = true;
                                break;
                            }
                        }
                    for (int k = 0; k < map.length; k++){
                        if(map[k][j] == 1 && k != i) {
                            exists = true;
                            break;
                        }
                    }
                    if(exists) count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args){
        System.out.println(getCommunicatingServersCount(new int[][]{  {1, 1, 1}, {1, 0, 1}, {1, 1, 1}}));
    }
}
