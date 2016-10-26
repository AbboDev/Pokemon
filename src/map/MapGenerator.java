package map;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Thomas
 */
public class MapGenerator {
    private static final int minCoo = 1;
    private static final int maxCoo = 256;
    private String[][] mapTile;
    
    private ImageIcon sky = new ImageIcon("C:\\Users\\Thomas\\Desktop\\sky.png");
    private ImageIcon earth = new ImageIcon("C:\\Users\\Thomas\\Desktop\\earth.png");
    private ImageIcon player = new ImageIcon("C:\\Users\\Thomas\\Desktop\\player.png");
    private ImageIcon wall = new ImageIcon("C:\\Users\\Thomas\\Desktop\\wall.png");
    private ImageIcon water = new ImageIcon("C:\\Users\\Thomas\\Desktop\\water.png");

    public MapGenerator(String name, int height, int width) {
        mapTile = new String[height][width];
    }
    
    public MapGenerator create2DArrays(File csvMap) {
        MapGenerator mapArray;
        String path = csvMap.getPath();
        ArrayList<ArrayList<String>> tempMap = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (currentLine.isEmpty()) continue;
                ArrayList<String> row = new ArrayList<>();
                String[] value = currentLine.split(";");
                for (String string: value) {
                    if (!string.isEmpty()) {
                        row.add(string);
                    }
                }
                tempMap.add(row);
            }
        } catch(IOException e) {}
        
        int width = tempMap.get(0).size();
        int height = tempMap.size();
        
        mapArray = new MapGenerator(null, height, width);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                mapArray.mapTile[x][y] = tempMap.get(x).get(y);
            }
        }
        return mapArray;
    }

    public JPanel putTexture(File csvMap) {
        JPanel grid = new JPanel();
        create2DArrays(csvMap);
        grid.setLayout(new GridLayout(6, 6));
        return null;
    }
}
