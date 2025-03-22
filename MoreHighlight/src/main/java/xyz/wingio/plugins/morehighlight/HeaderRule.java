package xyz.wingio.plugins.morehighlight;

import com.discord.simpleast.core.parser.ParseSpec;
import com.discord.simpleast.core.parser.Parser;
import com.discord.simpleast.core.parser.Rule;
import com.discord.utilities.textprocessing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HeaderRule extends Rule<MessageRenderContext, HeaderNode<MessageRenderContext>, MessageParseState> {

    public HeaderRule() {
        // Updated regex to match headers correctly and avoid other syntax issues
        super(Pattern.compile("^(#+)\\s+(.+)$", Pattern.MULTILINE));
    }

    @Override
    public ParseSpec<MessageRenderContext, MessageParseState> parse(Matcher matcher, Parser<MessageRenderContext, ? super HeaderNode<MessageRenderContext>, MessageParseState> parser, MessageParseState state) {
        // Determine header level from number of # symbols
        int level = matcher.group(1).length();
        // Extract header content
        String content = matcher.group(2).trim();

        // Apply additional parsing for nested Markdown formatting
        String parsedContent = parseMarkdown(content);
        
        HeaderNode headerNode = new HeaderNode(parsedContent, level);
        return new ParseSpec<>(headerNode, state);
    }

    // Method to parse nested Markdown formatting within header content
    private String parseMarkdown(String content) {
        // Handle bold (**text** or __text__)
        content = content.replaceAll("\\*\\*(.*?)\\*\\*", "**$1**");
        content = content.replaceAll("__(.*?)__", "__$1__");
        // Handle italic (*text* or _text_)
        content = content.replaceAll("\\*(.*?)\\*", "*$1*");
        content = content.replaceAll("_(.*?)_", "_$1_");
        // Handle strikethrough (~~text~~)
        content = content.replaceAll("~~(.*?)~~", "~~$1~~");
        // Handle inline code (`code`)
        content = content.replaceAll("`([^`]+)`", "`$1`");
        return content;
    }
}
