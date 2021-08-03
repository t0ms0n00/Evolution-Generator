package agh.cs;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/* ustawienie parametrów z pliku json dla danej symulacji */

public class Parser {

    public Simulator parse() throws IOException, ParseException {
        Object obj = new JSONParser().parse
                (new FileReader("src\\parameters\\parameters.json"));
        JSONObject jo = (JSONObject) obj;
        long width = (long) jo.get("width");
        int w=(int) width;
        long height = (long) jo.get("height");
        int h=(int) height;
        double jungleRatio = (double) jo.get("jungleRatio");
        JungleMap map1=new JungleMap(w,h,jungleRatio);
        JungleMap map2=new JungleMap(w,h,jungleRatio);
        long animalsOnStart = (long) jo.get("animalsOnStart");
        int animals=(int) animalsOnStart;
        long startEnergy = (long) jo.get("startEnergy");
        int sE=(int) startEnergy;
        long moveEnergy = (long) jo.get("moveEnergy");
        int mE=(int) moveEnergy;
        long plantEnergy = (long) jo.get("plantEnergy");
        int pE=(int) plantEnergy;
        return new Simulator(map1,map2,animals,sE,mE,pE);
    }

}
