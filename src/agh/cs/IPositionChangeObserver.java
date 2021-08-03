package agh.cs;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2D oldPosition, Vector2D newPosition);
}
