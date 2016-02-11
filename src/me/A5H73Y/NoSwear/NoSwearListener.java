package me.A5H73Y.NoSwear;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.A5H73Y.NoSwear.NoSwear;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NoSwearListener implements Listener {
	public NoSwear plugin;
	final String NoSwearString = ChatColor.DARK_PURPLE + "[" + ChatColor.DARK_AQUA + "NoSwear" + ChatColor.DARK_PURPLE + "] " + ChatColor.DARK_RED;
	final HashSet<String> chars = new HashSet<String>(Arrays.asList(new String[] { "@@@@@", "#####", "*****", "'''''", ".....", "-----", "?????", "!!!!!", "?!?!?!", "\"\"\"\"\"", "&&&&&", "$$$$$", "%%%%%", "=====", "++++++" }));

	private final Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))");
	private final Pattern webPattern = Pattern.compile("([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,}(:[0-9]{1,5})?");

	private final HashSet<String> allowedWebsites = new HashSet<String>(Arrays.asList(new String[] { "frostcast.net", "mojang.com", "twitch.tv/frostcast", "facebook.com/frostcast", "twitter.com/frostcastgaming", "youtube.com/user/TheFrostCastChannel" }));
	private HashSet<String> playerNames = new HashSet<String>();

	public HashMap<String, String> lastMessage = new HashMap<String, String>();

	public NoSwearListener(NoSwear instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		playerNames.add(event.getPlayer().getName());
		lastMessage.put(event.getPlayer().getName(), "");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerQuitEvent event) {
		playerNames.remove(event.getPlayer().getName());
		lastMessage.remove(event.getPlayer().getName());

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		if (event.getPlayer().hasPermission("noswear.ignore") && event.getPlayer().hasPermission("noswear.caps")) {
			return;
		}

		Player player = event.getPlayer();

		final String message = event.getMessage();

		String causeMessage = null;

		boolean cancel = false;

		main: if (!player.hasPermission("noswear.ignore")) {

			final String lowerCase = message.toLowerCase();

			final String[] split;

			String replaceColor = message;

			// tests to see if you said that message before
			if (message.equalsIgnoreCase(lastMessage.get(player.getName()))) {
				causeMessage = "Please do not repeat the same message twice!";
				cancel = true;
				break main;

			} else {
				lastMessage.put(player.getName(), message);

			}

			// checks for color spam
			int foundColors = 0;

			for (ChatColor value : ChatColor.values()) {
				if (message.replace(" ", "").contains(value.toString())) {
					foundColors++;

				}
				replaceColor = replaceColor.replace(value.toString(), "");

			}

			if (foundColors > 2) {
				causeMessage = "Please do not use more than 2 colours!";
				event.setMessage(replaceColor);
				cancel = false;
				break main;

			}

			split = replaceColor.split(" ");

			replaceColor = replaceColor.replace(" ", "");

			replaceColor = replaceColor.toLowerCase();

			// basic swearing test
			for (String swear : NoSwear.words) {

				if (replaceColor.contains(swear)) {

					boolean hasSworn = true;

					boolean searchedForPlayerName = false;

					exception: for (String phrase : NoSwear.exceptionPhrases) {

						if (lowerCase.contains(phrase)) {

							player.sendMessage(phrase);

							String replacePhrase = lowerCase.replace(phrase, "_");

							String replacePhraseAir = replacePhrase.replace(" ", "");

							if (replacePhraseAir.contains(swear)) {

								searchedForPlayerName = true;

								String lowerCaseWord;

								for (String word : replacePhrase.split(" ")) {

									lowerCaseWord = word.toLowerCase();

									player.sendMessage(lowerCaseWord);

									if (lowerCaseWord.contains(swear)) {
										if (playerNames.contains(word)) {

											if (replacePhraseAir.replace(lowerCaseWord, "_").contains(swear)) {
												hasSworn = true;
												break exception;

											}

											hasSworn = false;

										} else {
											hasSworn = true;
											break exception;

										}
									}
								}

								if (hasSworn != false) {

									break exception;

								}

							}

							hasSworn = false;

						}
					}

					if (hasSworn && !searchedForPlayerName) {
						String lowerCaseWord;

						for (String word : split) {

							lowerCaseWord = word.toLowerCase();

							if (lowerCaseWord.contains(swear)) {
								if (playerNames.contains(word)) {
									if (replaceColor.replace(lowerCaseWord, "_").contains(swear)) {
										hasSworn = true;
										break;

									}

									hasSworn = false;

								} else {
									hasSworn = true;
									break;

								}
							}
						}
					}

					if (hasSworn) {
						causeMessage = swearWarning(swear, replaceColor);
						cancel = true;
						break main;

					}
				}
			}

			// checks for more swearing
			for (String characters : chars) {
				if (replaceColor.contains(characters)) {
					causeMessage = "Please do not say \"" + ChatColor.DARK_RED + characters + "\".";
					cancel = true;
					break main;
				}

			}

			// checks for advertising

			if (ip(lowerCase)) {
				causeMessage = "Please do not list server IPs in public chat!";
				cancel = true;
				break main;

			} else if (website(lowerCase)) {
				causeMessage = "Please do not link websites in public chat!";
				cancel = true;
				break main;

			}

			// checks for caps

			if (!player.hasPermission("noswear.caps") && (message.length() > 3)) {

				int messageCaps = 0;
				int upperCase;

				for (String word : split) {

					if (word.length() == 1) {
						if (Character.isUpperCase(word.charAt(0))) {
							messageCaps++;

						}
						continue;

					}

					upperCase = 0;

					for (int i = 0; i < word.length(); i++) {
						if (Character.isUpperCase(word.charAt(i))) {
							upperCase++;

						}
					}

					if (upperCase > 3) {
						if (!playerNames.contains(word)) {
							causeMessage = "Please do not use excessive caps!";
							event.setMessage(message.toLowerCase());
							break main;

						}
						continue;

					}

					if (upperCase < 2) {
						continue;

					}

					messageCaps += upperCase;

				}

				if (messageCaps > 3) {
					causeMessage = "Please do not use excessive caps!";
					event.setMessage(message.toLowerCase());

				}
			}
		}

		if (causeMessage != null) {

			event.setCancelled(cancel);

			player.sendMessage(NoSwearString + causeMessage);
		}
	}

	public String swearWarning(String swear, String msg) {
		return "Please do not say \"" + ChatColor.DARK_RED + swear + "\". " + ChatColor.RED + "Where you said it: " + ChatColor.GOLD + msg.replace(swear, ChatColor.DARK_RED + swear + ChatColor.GOLD);

	}

	public boolean ip(String message) {
		Matcher regexMatcher = ipPattern.matcher(message);

		while (regexMatcher.find()) {
			if (regexMatcher.group().length() != 0) {
				if (ipPattern.matcher(message).find()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean website(String message) {
		Matcher regexMatcherurl = webPattern.matcher(message);

		while (regexMatcherurl.find()) {
			String text = regexMatcherurl.group().trim().replaceAll("www.", "").replaceAll("http://", "").replaceAll("https://", "");
			if (text.length() != 0) {
				boolean warn = true;

				for (String site : allowedWebsites) {
					if (text.contains(site))
						warn = false;
				}

				return warn;
			}
		}
		return false;
	}
}