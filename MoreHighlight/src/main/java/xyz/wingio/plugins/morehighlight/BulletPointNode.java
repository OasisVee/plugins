package xyz.wingio.plugins.morehighlight;

import com.aliucord.utils.DimenUtils;
import com.discord.simpleast.core.node.Node;
import com.discord.utilities.color.ColorCompat;

import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.*;

import com.lytefast.flexinput.R;
import com.aliucord.PluginManager;

public class BulletPointNode<MessageRenderContext> extends Node.a<MessageRenderContext> {
  final private Context context;
  final private String bulletType;

  public BulletPointNode(Context context, String bulletType){
    super();
    this.context = context;
    this.bulletType = bulletType;
  }

  @Override
  public void render(SpannableStringBuilder builder, MessageRenderContext renderContext) {
    int length = builder.length();
    super.render(builder, renderContext);

    int greyColor = ColorCompat.getThemedColor(context, R.b.colorTextMuted);
    boolean useStarBullet = PluginManager.plugins.get("MoreHighlight").settings.getBool("use_star_bullets", false);
    
    if (useStarBullet) {
      int start = builder.length();
      String starType = "*".equals(bulletType) ? "★ " : "☆ ";
      builder.insert(length, starType);
      builder.setSpan(new ForegroundColorSpan(greyColor), length, length + 2, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    } else {
      BulletSpan span = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P 
        ? new BulletSpan(/* gapWidth = */ DimenUtils.dpToPx(8), greyColor, /* bulletRadius = */ 6) 
        : new BulletSpan(/* gapWidth = */ DimenUtils.dpToPx(8), greyColor);
      
      builder.setSpan(span, length, builder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    
    builder.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), length, builder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
  }
}