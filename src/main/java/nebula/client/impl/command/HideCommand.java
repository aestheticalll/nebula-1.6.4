package nebula.client.impl.command;

import nebula.client.Nebula;
import nebula.client.api.command.Command;
import nebula.client.api.command.CommandMeta;
import nebula.client.api.command.CommandResults;
import nebula.client.api.command.exception.CommandFailureException;
import nebula.client.api.module.Module;

/**
 * @author Gavin
 * @since 08/25/23
 */
@SuppressWarnings("unused")
@CommandMeta(aliases = { "hide", "show", "drawn" },
    description = "Hide a module from the arraylist",
    syntax = "[module name]")
public class HideCommand extends Command
{
  @Override
  public int execute(String[] args) throws Exception
  {

    if (args.length == 0) return CommandResults.INVALID_SYNTAX;

    Module module = null;
    for (Module m : Nebula.INSTANCE.module.values())
    {
      if (m.meta().name().toLowerCase().startsWith(args[0].toLowerCase()))
      {
        module = m;
        break;
      }
    }

    if (module == null) throw new CommandFailureException(
        "No module found with that name");

    module.setHidden(!module.hidden());
    return CommandResults.SUCCESS;
  }
}
