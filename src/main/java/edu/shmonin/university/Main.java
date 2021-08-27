package edu.shmonin.university;

public class Main {

    public static void main(String[] args) {
        var university = new University();
        var menu = new MenuRunner(university);
        menu.run();
    }

}
