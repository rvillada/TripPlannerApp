package com.codepath.tripplannerapp;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children = null;
    private Address value;

    public Node(Address address) {
        this.children = new ArrayList<>();
        this.value = value;
    }

    public void addChild(Node child)
    {
        children.add(child);
    }
}
