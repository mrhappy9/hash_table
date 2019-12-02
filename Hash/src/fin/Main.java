package fin;

public class Main {

    public static void main(String[] args) {
        Pair<String, String> string = new Pair<>();
        System.out.println(string.insert("a", "c"));
        System.out.println(string.get("a"));
        System.out.println(string.delete("a"));
        System.out.println(string.get("a"));
    }
}
