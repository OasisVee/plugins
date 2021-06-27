package com.aliucord.plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aliucord.PluginManager;
import com.aliucord.Utils;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.widgets.LinearLayout;
import com.discord.api.channel.Channel;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.api.commands.CommandChoice;
import com.discord.app.AppBottomSheet;
import com.discord.models.commands.ApplicationCommandOption;
import com.discord.models.member.GuildMember;
import com.discord.models.user.User;
import com.discord.utilities.color.ColorCompat;
import com.discord.utilities.user.UserUtils;
import com.discord.views.CheckedSetting;
import com.discord.views.RadioManager;
import com.lytefast.flexinput.R$b;
import java.util.*;

@SuppressWarnings({ "unchecked", "unused" })
public class FriendNicknames extends Plugin {

  @NonNull
  @Override
  public Manifest getManifest() {
    var manifest = new Manifest();
    manifest.authors =
      new Manifest.Author[] {
        new Manifest.Author("Wing", 298295889720770563L),
      };
    manifest.description = "Set custom nicknames for each of your friends!";
    manifest.version = "1.0.3";
    manifest.updateUrl =
      "https://raw.githubusercontent.com/wingio/plugins/builds/updater.json";
    return manifest;
  }

  @Override
  @SuppressWarnings({ "unchecked", "ConstantConditions" })
  public void start(Context context) {
    patcher.patch(
      GuildMember.Companion.getClass(),
      "getNickOrUsername",
      new Class<?>[] {
        User.class,
        GuildMember.class,
        Channel.class,
        List.class,
      },
      new PinePatchFn(
        callFrame -> {
          var user = (User) callFrame.args[0];
          var userId = user.getId();
          var nickname = sets.getString(String.valueOf(userId), "NO_NICKNAME_FOUND");
          Utils.log(nickname);
          if (nickname == "NO_NICKNAME_FOUND") return;
          callFrame.setResult(nickname);
        }
      )
    );
    var userOption = new ApplicationCommandOption(
      ApplicationCommandType.STRING,
      "user",
      "ID of the User you want to set a nickname to",
      null,
      true,
      true,
      null,
      null
    );
    var nickOption = new ApplicationCommandOption(
      ApplicationCommandType.STRING,
      "nickname",
      "The nickname",
      null,
      true,
      true,
      null,
      null
    );

    var setOption = new ApplicationCommandOption(
      ApplicationCommandType.SUBCOMMAND,
      "set",
      "Set a nickname",
      null,
      false,
      false,
      null,
      Arrays.asList(userOption, nickOption)
    );

    var clearOption = new ApplicationCommandOption(
      ApplicationCommandType.SUBCOMMAND,
      "clear",
      "Clear a nickname",
      null,
      false,
      false,
      null,
      Arrays.asList(userOption)
    );

    commands.registerCommand(
      "nick",
      "Modify a nickname for a particular user",
      Arrays.asList(setOption, clearOption),
      args -> {
        if (args.containsKey("set")) {
          var id = (String) args.get("user");
          var nick = (String) args.get("nickname");
          Utils.log(id);
          Utils.log(nick);
          sets.setString(id, nick);
          return new CommandsAPI.CommandResult(
            "Set nickname successfuly",
            null,
            false
          );
        }
        if (args.containsKey("clear")) {
          var id = (String) args.get("user");
          sets.setString(String.valueOf(id), null);
          return new CommandsAPI.CommandResult(
            "Cleared nickname successfuly",
            null,
            false
          );
        }
        return new CommandsAPI.CommandResult();
      }
    );
  }

  @Override
  public void stop(Context context) {
    patcher.unpatchAll();
    commands.unregisterAll();
  }
}
