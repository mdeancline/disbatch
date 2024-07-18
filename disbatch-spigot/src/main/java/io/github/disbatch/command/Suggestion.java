package io.github.disbatch.command;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a suggestion that can be provided to the user during command input.
 * {@code Suggestion}s are typically used to assist users by offering potential completions or options
 * based on their current input.
 *
 * @see Suggestion#of(String, String)
 * @see Suggestion#ofTexts(Collection)
 * @since 1.1.0
 */
public final class Suggestion {
    private final String text;
    private final String tooltip;

    private Suggestion(final String text, final String tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    public static List<String> toTexts(final @NotNull Collection<Suggestion> suggestions) {
        final List<String> texts = new ArrayList<>(suggestions.size());

        for (final Suggestion suggestion : suggestions)
            texts.add(suggestion.getText());

        return texts;
    }

    /**
     * Creates a collection of {@code Suggestion}s from a collection of text strings.
     * Each string in the provided collection will be converted to a {@code Suggestion} with an
     * empty tooltip.
     *
     * @param texts the collection of text strings to convert to suggestions
     * @return a collection of suggestions
     */
    public static Collection<Suggestion> ofTexts(final @NotNull String... texts) {
        return ofTexts(Lists.newArrayList(texts));
    }

    /**
     * Creates a collection of {@code Suggestion}s from a collection of text strings.
     * Each string in the provided collection will be converted to a {@code Suggestion} with an
     * empty tooltip.
     *
     * @param texts the collection of text strings to convert to suggestions
     * @return a collection of suggestions
     */
    public static Collection<Suggestion> ofTexts(final @NotNull Collection<String> texts) {
        final List<Suggestion> suggestions = new ArrayList<>();

        for (final String text : texts)
            suggestions.add(of(text));

        return suggestions;
    }

    /**
     * Creates a {@code Suggestion} from a text string with an empty tooltip.
     *
     * @param text the text of the suggestion
     * @return a new suggestion with the specified text and an empty tooltip
     */
    public static Suggestion of(final @NotNull String text) {
        return of(text, StringUtils.EMPTY);
    }

    /**
     * Creates a {@code Suggestion} from a text string and a tooltip.
     *
     * @param text the text of the suggestion
     * @param tooltip the tooltip of the suggestion
     * @return a new suggestion with the specified text and tooltip
     */
    public static Suggestion of(final @NotNull String text, final @NotNull String tooltip) {
        return new Suggestion(text, tooltip);
    }

    /**
     * Gets the text of the suggestion. This text is what the user will see and can select
     * as a completion for their current input.
     *
     * @return the text of the suggestion
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the tooltip of the suggestion. The tooltip provides additional information
     * or context about the suggestion, which can help the user understand what the
     * suggestion represents or how it can be used.
     *
     * @return the tooltip of the suggestion
     */
    public String getTooltip() {
        return tooltip;
    }
}
