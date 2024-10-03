package nebula.client.account;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

/**
 * @param username the username of the cracked account
 * @author Gavin
 * @since 08/13/23
 */
public record Account(String username)
{

  public void login()
  {
    Minecraft.getMinecraft().setSession(new Session(username, "0", "legacy"));
  }
}
