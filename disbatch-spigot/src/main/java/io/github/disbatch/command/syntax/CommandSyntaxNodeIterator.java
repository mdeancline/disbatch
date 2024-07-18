package io.github.disbatch.command.syntax;

import java.util.Iterator;

public class CommandSyntaxNodeIterator implements Iterator<CommandSyntax.Node> {
    private CommandSyntax.Node current;
    private final boolean traverseTree;

    public CommandSyntaxNodeIterator(final CommandSyntax.Node current, boolean traverseTree) {
        this.current = current;
        this.traverseTree = traverseTree;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CommandSyntax.Node next() {
        return null;
    }
}
