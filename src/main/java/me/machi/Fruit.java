package me.machi;

import java.util.Objects;

public class Fruit {
    public String _id;
    public String name;
    public String description;

    public Fruit(String _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public Fruit() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fruit fruit = (Fruit) o;
        return Objects.equals(_id, fruit._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
