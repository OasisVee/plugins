package xyz.wingio.plugins.morehighlight;

import com.discord.simpleast.core.node.Node;
import com.discord.utilities.color.ColorCompat;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.*;

import com.lytefast.flexinput.R;

public class SubtextNode<MessageRenderContext> extends Node.a<MessageRenderContext> {
  private final Context context;

  public SubtextNode(Context context){
    super();
    this.context = context;
  }

  @Override
  public void render(SpannableStringBuilder builder, MessageRenderContext renderContext) {
    int length = builder.length();
    super.render(builder, renderContext);

    builder.setSpan(new RelativeSizeSpan(0.85f), length, builder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

    int greyColor = ColorCompat.getThemedColor(context, R.b.colorTextMuted);
    builder.setSpan(new ForegroundColorSpan(greyColor), length, builder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
  }
}
