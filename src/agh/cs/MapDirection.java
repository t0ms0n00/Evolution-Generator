package agh.cs;

/* enum z 8 kierunkami jak się może poruszyć zwierzę, zawiera funkcję zwracającą kierunek jako tekst, wybierającą kolejny kierunek
oraz przerabiającą kierunek na wektor
 */

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString(){
        switch(this) {
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case EAST: return "Wschód";
            case WEST: return "Zachód";
            case NORTHEAST: return "PółnocnyWschód";
            case SOUTHEAST: return "PołudniowyWschód";
            case SOUTHWEST: return "PołudniowyZachód";
            case NORTHWEST: return "PółnocnyZachód";
            default: return "Error";
        }
    }

    public MapDirection next(){
        switch(this) {
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case NORTHWEST:return NORTH;
            default: throw new IllegalArgumentException("Switch czegoś nie uwzględnia");
        }
    }

    public Vector2D toUnitVector(){
        switch(this) {
            case NORTH: return new Vector2D(0,1);
            case SOUTH: return new Vector2D(0,-1);
            case EAST: return new Vector2D(1,0);
            case WEST: return new Vector2D(-1,0);
            case NORTHEAST: return new Vector2D(1,1);
            case NORTHWEST: return new Vector2D(-1,1);
            case SOUTHEAST: return new Vector2D(1,-1);
            case SOUTHWEST: return new Vector2D(-1,-1);
            default: return new Vector2D(0,0);
        }
    }
}
