package agh.cs;

import java.util.Objects;

public class Vector2D {
    final int x;
    final int y;

    /* konstruktor wektora */

    public Vector2D(int x,int y){
        this.x=x;
        this.y=y;
    }

    /* metoda konwertująca wektor na typ tekstowy */

    public String toString(){
        return ("("+this.x+", "+this.y+")");
    }

    /* obliczanie funkcji hashujacej */

    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    /* dla pary wektorów tworzy wektor o współrzędnych, które są większe */

    public Vector2D upperRight(Vector2D other){
        int x;
        int y;
        if (this.x>=other.x) x=this.x;
        else x=other.x;
        if (this.y>=other.y) y=this.y;
        else y=other.y;
        Vector2D score=new Vector2D(x,y);
        return score;
    }

    /* dla pary wektorów tworzy wektor o współrzędnych, które są mniejsze */

    public Vector2D lowerLeft(Vector2D other){
        int x;
        int y;
        if (this.x>=other.x) x=other.x;
        else x=this.x;
        if (this.y>=other.y) y=other.y;
        else y=this.y;
        Vector2D score=new Vector2D(x,y);
        return score;
    }

    /* suma dwóch wektorów */

    public Vector2D add(Vector2D other){
        int x=this.x+other.x;
        int y=this.y+other.y;
        Vector2D score=new Vector2D(x,y);
        return score;
    }

    /* różnica dwóch wektorów */

    public Vector2D subtract(Vector2D other){
        int x=this.x-other.x;
        int y=this.y-other.y;
        Vector2D score=new Vector2D(x,y);
        return score;
    }

    /* porównywanie wektorów */

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2D)) return false;
        Vector2D that = (Vector2D) other;
        if (that.x==this.x && that.y==this.y) return true;
        else return false;
    }

}
