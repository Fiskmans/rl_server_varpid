package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("varp_finder")
public interface VarpFinderConfig extends Config
{
	@ConfigItem(
		keyName = "value",
		name = "Value",
		description = "The value to look for in varplayer variables"
	)
	default int value()
	{
		return 0;
	}
}
