package xyz.wingio.plugins.morehighlight;

import android.annotation.SuppressLint;
import android.view.*;
import android.widget.*;
import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.res.ResourcesCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.*;

import xyz.wingio.plugins.MoreHighlight;

import com.aliucord.Constants;
import com.aliucord.Utils;
import com.aliucord.utils.*;
import com.aliucord.Http;
import com.aliucord.Logger;
import com.aliucord.PluginManager;
import com.aliucord.api.SettingsAPI;
import com.aliucord.api.NotificationsAPI;
import com.aliucord.fragments.SettingsPage;
import com.aliucord.views.Divider;
import com.aliucord.entities.NotificationData;

import com.discord.views.CheckedSetting;
import com.discord.views.RadioManager;
import com.discord.widgets.user.profile.UserProfileHeaderView;
import com.discord.stores.*;
import com.discord.models.user.User;
import com.discord.panels.*;
import com.discord.utilities.rest.RestAPI;
import com.discord.utilities.analytics.AnalyticSuperProperties;
import com.discord.utilities.color.ColorCompat;
import com.lytefast.flexinput.R;

import kotlin.Unit;
import java.util.*;

@SuppressLint("SetTextI18n")
public final class PluginSettings extends SettingsPage {
    private SettingsAPI settings;
    private MoreHighlight plugin;
    private int p = DimenUtils.dpToPx(16);
    
    public PluginSettings(MoreHighlight plugin) {
        this.plugin = plugin;
        this.settings = plugin.settings;
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onViewBound(View view) {
        super.onViewBound(view);
        setActionBarTitle("MoreHighlight Settings");
        setPadding(p);

        var ctx = view.getContext();
        var layout = getLinearLayout();
        
        layout.addView(createSwitch(ctx, settings, "show_repo_name", "Show repo name in issue/pr link", null, false));
        
        TextView bulletHeader = new TextView(ctx, null, 0, R.i.UiKit_Settings_Item_Label);
        bulletHeader.setText("Bullet Point Settings");
        bulletHeader.setPadding(0, p, 0, 0);
        layout.addView(bulletHeader);
        
        TextView bulletDescription = new TextView(ctx, null, 0, R.i.UiKit_Settings_Item_SubText);
        bulletDescription.setText("Configure which bullet point types to enable. Enabling both options will disable all bullet points.");
        bulletDescription.setPadding(p, 0, p, DimenUtils.dpToPx(8));
        layout.addView(bulletDescription);
        
        layout.addView(createSwitch(ctx, settings, "disable_hyphen_bullets", "Disable hyphen bullet points", "Disables bullet points that start with a hyphen (-)", false));
        layout.addView(createSwitch(ctx, settings, "disable_asterisk_bullets", "Disable asterisk bullet points", "Disables bullet points that start with an asterisk (*)", false));
        layout.addView(createSwitch(ctx, settings, "use_star_bullets", "Use star bullets", "Use stars instead of standard bullet points (- to ☆ and * to ★)", false));
        
        TextView headerSizeLabel = new TextView(ctx, null, 0, R.i.UiKit_Settings_Item_Label);
        headerSizeLabel.setText("Header Size Scale");
        headerSizeLabel.setPadding(0, p, 0, 0);
        layout.addView(headerSizeLabel);
        
        TextView headerSizeDescription = new TextView(ctx, null, 0, R.i.UiKit_Settings_Item_SubText);
        headerSizeDescription.setText("Enter a value between 0.2 and 2.0 (1.0 = default)");
        headerSizeDescription.setPadding(p, 0, p, DimenUtils.dpToPx(8));
        layout.addView(headerSizeDescription);

        EditText headerSizeInput = new EditText(ctx);
        float currentScale = settings.getFloat("header_size_scale", 1.0f);
        headerSizeInput.setText(String.valueOf(currentScale));
        headerSizeInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        headerSizeInput.setHint("1.0");
        headerSizeInput.setPadding(p, DimenUtils.dpToPx(8), p, p);
        
        headerSizeInput.setTextColor(ColorCompat.getThemedColor(ctx, R.b.colorTextNormal));
        headerSizeInput.setHintTextColor(ColorCompat.getThemedColor(ctx, R.b.colorTextMuted));
        
        layout.addView(headerSizeInput);

        Button applyButton = new Button(ctx);
        applyButton.setText("Apply");
        applyButton.setTextColor(ColorCompat.getThemedColor(ctx, R.b.colorTextNormal));
        applyButton.setOnClickListener(v -> {
            try {
                float scale = Float.parseFloat(headerSizeInput.getText().toString());
                scale = Math.max(0.2f, Math.min(2.0f, scale));
                settings.setFloat("header_size_scale", scale);
                headerSizeInput.setText(String.valueOf(scale));
                
                Utils.showToast("Header size scale updated to " + scale, true);
            } catch (NumberFormatException e) {
                Utils.showToast("Please enter a valid number", false);
                headerSizeInput.setText(String.valueOf(currentScale));
            }
        });
        layout.addView(applyButton);
        
        layout.addView(new Divider(ctx));
        
        TextView info = new TextView(ctx, null, 0, R.i.UiKit_TextView);
        info.setText(MDUtils.render("**Currently supports:**\n\n" +
                "- **Reddit** (<r/[Subreddit]>, <u/[User]>, *ex. <r/Aliucord>*)\n" +
                "- **Github** (<username/repo#issue> <gh:username/repo>, *ex. <Aliucord/Aliucord#127>*)\n" +
                "- **Plugin Settings** (ac://[Plugin Name], *ex. ac://MoreHighlight*)\n" +
                "- **Colors** *ex #1f8b4c*\n" +
                "- **Slash Commands** (</command:id>, *ex. </airhorn:816437322781949972>*)\n" +
                "- **Headers** (# Header 1, ## Header 2, ### Header 3)\n" +
                "- **Subtext** (-# tiny greyed out text)\n" +
                "- **BulletPoints** (* bulletpoint or - bulletpoint)"));
        info.setPadding(0, p, 0, 0);
        layout.addView(info);
    }

    private CheckedSetting createSwitch(Context context, SettingsAPI sets, String key, String label, String subtext, boolean defaultValue) {
        CheckedSetting cs = Utils.createCheckedSetting(context, CheckedSetting.ViewType.SWITCH, label, subtext);
        cs.setChecked(sets.getBool(key, defaultValue));
        cs.setOnCheckedListener(c -> sets.setBool(key, c));
        return cs;
    }
}