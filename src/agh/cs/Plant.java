package agh.cs;

public class Plant {

    private Vector2D position;
    private int unitEnergy=5;

    /* domyślnie energia wzięta taka, aczkolwiek używany tylko konstruktor, który ustawia energię, ten przypadek był tylko
    jako początkowy do testów
     */

    public Plant(Vector2D position){
        this.position=position;
    }

    public Plant(Vector2D position, int unitEnergy){
        this.unitEnergy=unitEnergy;
        this.position=position;
    }

    /* zwraca położenie rośliny*/

    public Vector2D getPosition(){
        return this.position;
    }

    /* zwraca energie jaką posiada roślina */

    public int getUnitEnergy() { return this.unitEnergy; }
}
