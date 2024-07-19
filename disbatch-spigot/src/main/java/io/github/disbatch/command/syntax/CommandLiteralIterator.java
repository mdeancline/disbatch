package io.github.disbatch.command.syntax;

import java.util.Iterator;

public class CommandLiteralIterator implements Iterator<CommandSyntax.Literal> {
    private CommandSyntax.Literal current;
    private final boolean traverseTree;

    public CommandLiteralIterator(final CommandSyntax.Literal current, boolean traverseTree) {
        this.current = current;
        this.traverseTree = traverseTree;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CommandSyntax.Literal next() {
        return null;
    }
}