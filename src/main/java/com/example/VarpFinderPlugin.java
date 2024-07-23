package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Varp Finder"
)
public class VarpFinderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private VarpFinderConfig config;

	@Inject
	public ClientThread clientThread;

	Map<Integer, Integer> myLookupTable = new HashMap<>();

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	void onVarbitChanged(VarbitChanged event)
	{
		myLookupTable.put(event.getVarpId(), event.getValue());

		if (event.getValue() == config.value())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VarPlayer with id [ " + event.getVarpId() + "] equals the configured value", "varp_finder_plugin");
		}
	}

	@Subscribe
	void onConfigChanged(ConfigChanged configChanged)
	{
		if (!configChanged.getGroup().equals("varp_finder"))
			return;

		clientThread.invokeLater(() ->
		{
			myLookupTable.forEach( (key, value) -> {
				if (value.equals(config.value()))
				{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VarPlayer with id [ " + key + "] equals the configured value", "varp_finder_plugin");
				}
			});
		});
	}

	@Provides
	VarpFinderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VarpFinderConfig.class);
	}
}
