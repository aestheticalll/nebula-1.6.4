package nebula.client;

import io.sentry.Sentry;
import nebula.client.api.account.AccountRegistry;
import nebula.client.api.command.CommandRegistry;
import nebula.client.api.config.ConfigLoader;
import nebula.client.api.friend.FriendRegistry;
import nebula.client.api.gui.font.Fonts;
import nebula.client.api.inventory.InventoryManager;
import nebula.client.api.eventbus.EventBus;
import nebula.client.api.macro.MacroRegistry;
import nebula.client.api.module.ModuleRegistry;
import nebula.client.api.rotation.RotationSpoofer;
import nebula.client.util.fs.FileUtils;
import nebula.client.api.Registry;
import nebula.client.util.render.RenderUtils;
import net.minecraft.crash.CrashReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Gavin
 * @since 08/09/23
 */
public enum Nebula
{

  /**
   * The client instance
   */
  INSTANCE;

  /**
   * The event bus instance
   */
  public static final EventBus BUS = new EventBus();

  /**
   * The logger instance
   */
  public static final Logger LOGGER = LogManager.getLogger(
      "Nebula");

  /**
   * The executor
   */
  public static final Executor EXECUTOR = Executors.newFixedThreadPool(1);

  public MacroRegistry macro;
  public CommandRegistry command;
  public ModuleRegistry module;
  public FriendRegistry friend;
  public AccountRegistry account;
  public InventoryManager inventory;
  public RotationSpoofer rotation;

  /**
   * Initializes the client
   */
  public void init()
  {
    LOGGER.info("Starting Nebula");
    fakeCrashReport();

    // init sentry
    Sentry.init(options ->
    {
      options.setDsn("https://babcbe375647d8cdabbb8f84755d645b@o4505681333125120.ingest.sentry.io/4505681334501376");
      // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
      // We recommend adjusting this value in production.
      options.setTracesSampleRate(1.0);
    });

    // testSentry();

    // set client title
    Display.setTitle("Nebula "
        + BuildConfig.VERSION
        + "+" + BuildConfig.BUILD
        + "-" + BuildConfig.HASH
        + "/" + BuildConfig.BRANCH);

    // create file
    if (!FileUtils.ROOT.exists())
    {
      boolean result = FileUtils.ROOT.mkdir();
      LOGGER.info("Created {} {}",
          FileUtils.ROOT,
          result ? "successfully" : "unsuccessfully");
      if (!result)
      {
        throw new RuntimeException("Could not create directory %s"
            .formatted(FileUtils.ROOT.getAbsolutePath()));
      }
    }

    // create registries
    macro = new MacroRegistry();
    command = new CommandRegistry();
    module = new ModuleRegistry();
    friend = new FriendRegistry();
    account = new AccountRegistry();

    // init registries
    initRegistriesOrdered(macro, command, module, friend, account);

    // other misc shit
    inventory = new InventoryManager();
    rotation = new RotationSpoofer();

    RenderUtils.loadShaders();
    Fonts.loadFonts();

    Runtime.getRuntime().addShutdownHook(new Thread(() ->
    {
      ConfigLoader.save();

      try
      {
        LOGGER.info(module.save("default"));
      } catch (IOException e)
      {
        LOGGER.error("Failed to save default config");
        e.printStackTrace();
        Sentry.captureException(e);
      }
    }, "Nebula Shutdown Hook"));
  }

  /**
   * Runs additional required tasks after the main components are loaded
   */
  public void postInit()
  {
    ConfigLoader.load();
  }

  /**
   * Initializes registries in a given order
   *
   * @param registries the registries to call {@link Registry#init()} on
   */
  void initRegistriesOrdered(Registry<?>... registries)
  {
    LOGGER.info("{} registries to initialize", registries.length);

    int registered = 0;
    for (Registry<?> registry : registries)
    {
      try
      {
        registry.init();
        ++registered;
      } catch (Exception e)
      {
        LOGGER.error("Failed to initialize registry {}", registry);
        e.printStackTrace();
        Sentry.captureException(e);
      }
    }

    LOGGER.info("Initialized {}/{} registries", registered, registries.length);
  }

  /**
   * Generates a fake crash report showing the user hardware information
   * For more useful debugging if a game crashes/a graphical issue occurs
   */
  void fakeCrashReport()
  {
    LOGGER.warn("Generating fake crash report...");

    final CrashReport report = new CrashReport("This is not a real crash report. Please include this section if you are reporting an error.",
        new Throwable("Debugging Crash Report"));
    System.out.println(report.getCompleteReport());
  }

  void testSentry()
  {
    Sentry.captureException(new Throwable("test"));
  }
}