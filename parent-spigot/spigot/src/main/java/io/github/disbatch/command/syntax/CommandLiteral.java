package io.github.disbatch.command.syntax;

import io.github.disbatch.Command;

/**
 * Represents a specific label in the {@link CommandSyntax} of a {@link Command}. This label can be associated with
 * one or more parameters. If present, child literals representing adjacent label trees can be iterated over.
 *
 * @since 1.1.0
 */
public interface CommandLiteral extends Iterable<CommandLiteral> {

    /**
     * Retrieves the label of this literal.
     *
     * @return the label of the literal
     */
    String getLabel();
}

