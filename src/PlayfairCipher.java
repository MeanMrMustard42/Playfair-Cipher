import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

class PointHelper {
    int x = -1;
    int y = -1;
    public PointHelper(int first, int second) {
        x = first;
        y = second;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}

class BigramDictionary {
    private HashMap<String, String> dict = new HashMap<String, String>();

    public BigramDictionary(String[] bigrams) {
        for(int i = 0; i < bigrams.length-1; i++) {
        dict.put(bigrams[i], bigrams[i+1]); // num of bigrams should always be even so no issue of arrayindex exceptions
        }
    }

public HashMap<String, String> getBigramHashMap() {
    return dict;
}
}



class PlayfairGrid {
    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String[][] grid = new String[5][5];
    String[][] transpose = new String[5][5];

    private BigramDictionary knownBigrams;
    public PlayfairGrid(BigramDictionary dict) {
        knownBigrams = dict;
        fillGrid();
    }

    private void fillGrid() {
    HashMap<String, String> bigrams = knownBigrams.getBigramHashMap();
        for (Map.Entry<String, String> entry : bigrams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            placeBigrams(key, value);
            updateTranspose();
        }

    }

    private String[][] updateTranspose() {
        for (int i=0; i<grid.length-1; i++) {
            for (int j=i+1; j<grid.length; j++) {
                transpose[i][j] = grid[j][i];
            }
        }
        return transpose;
    }

    private ArrayList<PointHelper> getColumnNullValues(int i) {
        ArrayList<PointHelper> nullPlaces = new ArrayList<PointHelper>();
        updateTranspose();
        for(int j = 0; j < transpose[i].length; j++) {
            if(transpose[i][j] == null) {
                nullPlaces.add(new PointHelper(i, j));
            }

        }
        return nullPlaces;
    }

    private ArrayList<PointHelper> getRowNullValues(int i) {
        ArrayList<PointHelper> nullPlaces = new ArrayList<PointHelper>();
        for(int j = 0; j < grid[i].length; j++) {
            if(transpose[i][j] == null) {
                nullPlaces.add(new PointHelper(i, j));
            }

        }
        return nullPlaces;
    }
    private String getGridValue(int x, int y) {
        return grid[x][y];

    }
    private void setGridValue(int x, int y, String z) {
        grid[x][y] = z;
    }

    private void placeBigrams(String key, String value) {

        ArrayList<PointHelper> rowNullPoints = null;
        ArrayList<PointHelper> colNullPoints = null;

        for(int i = 0; i < grid.length; i++) {
           rowNullPoints = getRowNullValues(i);
           colNullPoints = getColumnNullValues(i);
        }
        if(colNullPoints.size() >= 2) {
            for(int i = 0; i < colNullPoints.size(); i++) { //column rule - shift one to right
                int rowValue = colNullPoints.get(i).getX();
                int colValue = colNullPoints.get(i).getY();
                setGridValue(rowValue, colValue, key); // put plaintext letter (i.e. "O") in place
                colValue+=1;
                if (colValue > 4) { // implenting the rule of wrapping around when the col number exceeds 5
                    colValue = 0;
                }
                if(getGridValue(rowValue, colValue) == null) {
                setGridValue(rowValue, colValue, value); // put ciphertext letter (i.e., "V") in place
                }
            }
            for(int i = 0; i < rowNullPoints.size(); i++) { //row rule - shift one up
                int rowValue = colNullPoints.get(i).getX();
                int colValue = colNullPoints.get(i).getY();
                setGridValue(rowValue, colValue, key); // put plaintext letter (i.e. "O") in place
                rowValue-=1;
                if (rowValue < 0) { // implenting the rule of wrapping around when the col number is less than 0
                    rowValue = 5;
                }
                if(getGridValue(rowValue, colValue) == null) {
                setGridValue(rowValue, colValue, value); // put ciphertext letter (i.e., "V") in place
                }
            }


        }
    }

    


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                sb.append(grid[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
}

public class PlayfairCipher {
    public static void main(String[] args) throws Exception {
        String[] BIGRAM_LIST = {"T", "U", "M", "N", "C", "S", "X", "W", "O", "N", "V", "U", "A", "R", "G", "X", "I", "Z", "O", "P"};
        BigramDictionary bigrams = new BigramDictionary(BIGRAM_LIST);
        PlayfairGrid grid = new PlayfairGrid(bigrams);
        System.out.println(grid.toString());
    }
}
