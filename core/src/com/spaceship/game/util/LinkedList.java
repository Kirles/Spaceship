package com.spaceship.game.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class LinkedList {
    private LinkedList.Node head;
    private LinkedList.Node tail;
    private int size;

    public LinkedList() {
    }

    public void add(Actor actor) {
        if (find(actor) == null) {
            LinkedList.Node node = new Node(actor);
            if (size == 0) {
                head = tail = node;
            } else {
                tail.next = node;
                node.previous = tail;
                tail = node;
            }
            size++;
        }

    }

    private LinkedList.Node find(Actor actor) {
        LinkedList.Node temp = head;
        while(temp != null && temp.value != actor){
            temp = temp.next;
        }
        return temp;
    }

    public Actor get(int position) {
        LinkedList.Node temp;
        for(temp = head; position > 0; --position) {
            temp = temp.next;
        }

        return temp.value;
    }

    public void removeAll() {
        while(head != null) {
            remove(head.value);
        }

    }

    public void remove(Actor actor) {
        LinkedList.Node node = find(actor);
        if (node.previous == null) {
            head = head.next;
            if (head != null) {
                head.previous = null;
            }
        }
        else if (node.next == null) {
            tail = node.previous;
            tail.next = null;
        }
        else {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }

        size--;
    }

    public int getSize() {
        return size;
    }

    private static class Node {
        Actor value;
        LinkedList.Node next;
        LinkedList.Node previous;

        Node(Actor value) {
            this.value = value;
            next = previous = null;
        }
    }
}