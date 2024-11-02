package nebula.client.impl.module.render.noweather;

import nebula.client.api.eventbus.Listener;
import nebula.client.api.eventbus.Subscribe;
import nebula.client.impl.event.render.weather.EventRainStrength;
import nebula.client.impl.event.render.weather.EventThunderStrength;
import nebula.client.api.module.Module;
import nebula.client.api.module.ModuleMeta;
import nebula.client.api.value.Setting;
import nebula.client.api.value.SettingMeta;

/**
 * @author Gavin
 * @since 08/24/23
 */
@SuppressWarnings("unused")
@ModuleMeta(name = "NoWeather",
    description = "rain rain go away come back another day")
public class NoWeatherModule extends Module
{

  @SettingMeta("Thunder")
  private final Setting<Boolean> thunder = new Setting<>(
      true);

  @Subscribe
  private final Listener<EventRainStrength> rainStrength
      = event -> event.setStrength(0.0f);

  @Subscribe
  private final Listener<EventThunderStrength> thunderStrength = event ->
  {
    if (!thunder.value()) return;
    event.setStrength(0.0f);
  };
}
