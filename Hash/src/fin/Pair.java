package fin;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Pair <K, V> {

    private Node[] hashtable;
    private int size = 0;
    private float threshold; // степень загруженности таблицы

    public Pair(){
        hashtable = new Node[16];
        threshold = hashtable.length * 0.75f;
    }

    public boolean insert(K key, V value){
        if(size + 1 >= threshold){
            threshold *= 2;
            refreshTable();
        }
        Node<K, V> node = new Node<>(key, value);
        int index = node.hash();

        if(hashtable[index] == null){
            return simpleAdd(index, node);
        }

        /*исключение дубликатов*/
        List<Node<K, V>> nodeList = hashtable[index].getNodes();
        for(Node<K, V> nodes: nodeList){
            if(keyExistButValueNew(nodes, node, value) || collisionProcessing(nodes, node, nodeList)){
                return true;
            }
        }
        return false;
    }

    private boolean simpleAdd(int index, Node<K, V> node){
        hashtable[index] = new Node<>(null, null);
        hashtable[index].getNodes().add(node);
        size++;
        return true;
    }

    private boolean keyExistButValueNew(Node<K, V> nodeFromList,Node<K, V> node,  V value){
        if(node.getKey().equals(nodeFromList.getKey()) &&
            !node.getValue().equals(nodeFromList.getValue())){
            nodeFromList.setValue(value);
            return true;
        }
        return false;
    }

    private boolean collisionProcessing(Node<K, V> nodeFromList,
                                        Node<K, V> node,
                                        List<Node<K, V>> nodes){
        if(node.hashCode() == nodeFromList.hashCode() &&
                !Objects.equals(node.key, nodeFromList.key) &&
                !Objects.equals(node.value, nodeFromList.value)){
            nodes.add(node);
            size++;
            return true;
        }
        return false;
    }

    private void refreshTable(){
        Node<K, V>[] oldHashTable = hashtable;
        hashtable = new Node[oldHashTable.length*2];
        size = 0;
        for(Node<K, V> node: oldHashTable){
            if (node != null){
                for(Node<K, V> n: node.getNodes()){
                    insert(n.key, n.value);
                }
            }
        }
    }

    public boolean delete(K key){
        int index = hash(key);
        if(hashtable[index] == null)
            return false;

        if(hashtable[index].getNodes().size() == 1){
            hashtable[index] = null;
            return true;
        }

        List<Node<K, V>> nodeList = hashtable[index].getNodes();
        for(Node<K, V> node: nodeList){
            if(key.equals(node.getKey())){
                nodeList.remove(node);
                return true;
            }
        }
        return false;
    }

    public V get(K key){
        int index = hash(key);
        if(index < hashtable.length && hashtable[index] != null){
            List<Node<K, V>> list = hashtable[index].getNodes();
            for(Node<K, V> node: list){
                if(key.equals(node.getKey())){
                    return node.getValue();
                }
            }
        }
        return null;
    }

    private int hash(K key){
        return (31*17 + key.hashCode()) % hashtable.length;
    }

    private int hash(Node<K, V> node){
        return node.hashCode() % hashtable.length;
    }

    private class Node<K, V>{
        private List<Node<K, V>> nodes;
        private int hash;
        private K key;
        private V value;

        private Node(K key, V value){
            this.key = key;
            this.value = value;
            nodes = new LinkedList<Node<K, V>>();
        }

        private List<Node<K, V>> getNodes(){
            return nodes;
        }

        private int hash(){
            return hashCode() % hashtable.length;
        }

        private K getKey(){
            return key;
        }

        private V getValue(){
            return value;
        }

        private void setValue(V value){
            this.value = value;
        }

        @Override
        public int hashCode(){
            return 31*17 + key.hashCode();
        }

        @Override
        public boolean equals(Object object){
            if(this == object)
                return true;
            if(object instanceof Node){
                Node<K, V> node = (Node) object;
                return(Objects.equals(key, node.getKey()) && Objects.equals(value, node.getValue()) ||
                        Objects.equals(hash, node.hashCode()));
            }
            return false;
        }
    }
}
