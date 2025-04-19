package xyz.wingio.plugins.morehighlight;

import android.content.Context;

import com.discord.simpleast.core.parser.ParseSpec;
import com.discord.simpleast.core.parser.Parser;
import com.discord.simpleast.core.parser.Rule;
import com.discord.utilities.textprocessing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BulletPointRule extends Rule.BlockRule<MessageRenderContext, BulletPointNode<MessageRenderContext>, MessageParseState> {
    final private Context context;
    final private Pattern pattern;

    public BulletPointRule(Context context, Pattern pattern) {
        super(pattern);
        this.context = context;
        this.pattern = pattern;
    }

    @Override
    public ParseSpec<MessageRenderContext, MessageParseState> parse(Matcher matcher, Parser<MessageRenderContext, ? super BulletPointNode<MessageRenderContext>, MessageParseState> parser, MessageParseState s) {
        String bulletType = matcher.group(1);
        return new ParseSpec<>(new BulletPointNode(context, bulletType), s, matcher.start(2), matcher.end(2));
    }
}