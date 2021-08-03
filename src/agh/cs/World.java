package agh.cs;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class World {

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        try{
            Parser jsonParser=new Parser();
            Simulator simulator = jsonParser.parse();
            simulator.run();
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex);
        }
    }
}