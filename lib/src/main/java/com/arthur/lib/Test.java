package com.arthur.lib;

public class Test {
    public static void main(String[] args) {
//        new MyClass().main();
    }

    class Node {
        public Node value;
        public Node next;
        public Node pre;
    }

    public Node reverse(Node head) {
        Node pre = null;
        Node cur = head;

        while (cur != null) {
            Node next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }

        return pre;
    }


}
