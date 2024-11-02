package nebula.client.impl.command;

import nebula.client.api.command.Command;
import nebula.client.api.command.CommandMeta;
import nebula.client.api.command.CommandResults;
import nebula.client.util.player.chat.Printer;
import net.minecraft.world.chunk.Chunk;

import static java.lang.String.format;

/**
 * @author Gavin
 * @since 08/25/23
 */
@SuppressWarnings("unused")
@CommandMeta(aliases = { "chunksize", "sizechunk", "size" },
    description = "Gets the size of the chunk you are in")
public class ChunkSizeCommand extends Command
{

  private static final int ONE_MB_BYTES = 1_000_000;
  private static final int ONE_GB_MB = 1000;

  @Override
  public int execute(String[] args) throws Exception
  {

    Chunk chunk = mc.theWorld.getChunkFromChunkCoords(
        mc.thePlayer.chunkCoordX, mc.thePlayer.chunkCoordZ);
    if (chunk == null) return CommandResults.SUCCESS;

    int size = chunk.chunkByteSize;
    if (size <= 0) return CommandResults.SUCCESS;

    double mb = (double) size / ONE_MB_BYTES;
    double gb = mb / ONE_GB_MB;

    Printer.print(format("Chunk[x=%s z=%s] = %.3fGB, %.3fMB (%s bytes)",
        chunk.xPosition, chunk.zPosition, gb, mb, size));

    return CommandResults.SUCCESS_NO_RESPONSE;
  }
}
