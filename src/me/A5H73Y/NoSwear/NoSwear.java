package me.A5H73Y.NoSwear;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NoSwear extends JavaPlugin {

	public static NoSwearListener plugin;

	public final NoSwearListener playerListener = new NoSwearListener(this);

	public static final int capsBlockerMinPercent = 30;

	public static final String[] words = 
		{ "1111"
		, "2222"
		, "3333"
		, "4444"
		, "5555"
		, "6666"
		, "7777"
		, "8888"
		, "8==="
		, "9999"
		, "aaaa"
		, "anal"
		, "arse"
		, "asshole"
		, "asshole"
		, "bastard"
		, "bastard"
		, "basterd"
		, "bbbb"
		, "bellend"
		, "biatch"
		, "bit@h"
		, "bitccch"
		, "bitch"
		, "bitch"
		, "bitch"
		, "blowjob"
		, "boob"
		, "bumed"
		, "cccc"
		, "ccoocckk"
		, "clit"
		, "cock"
		, "cock"
		, "cum"
		, "cum"
		, "cunt"
		, "cunt"
		, "d-i-c-k"
		, "d1ck"
		, "dafuq"
		, "dddd"
		, "ddiicckk"
		, "dick"
		, "dick"
		, "dildo"
		, "douche"
		, "dumbass"
		, "eeee"
		, "erection"
		, "f***"
		, "f*ck"
		, "f-ck"
		, "f-u-c-k"
		, "f-u-c.k"
		, "f.u.c.k"
		, "f.uck"
		, "f/u/c/k"
		, "f_uck"
		, "fack"
		, "fag"
		, "faget"
		, "faggot"
		, "fagots"
		, "feck"
		, "ffff"
		, "fingered"
		, "frack"
		, "frick"
		, "fu(ing"
		, "fu(k"
		, "fu.k"
		, "fu[k"
		, "fuc.k"
		, "fucck"
		, "fuck"
		, "fuck"
		, "fuk"
		, "fuuck"
		, "gay"
		, "gggg"
		, "hahaha"
		, "hhhh"
		, "iiii"
		, "jizz"
		, "jizz"
		, "jjjj"
		, "kkkk"
		, "knob"
		, "lalala"
		, "llll"
		, "lolol"
		, "masturbation"
		, "milf"
		, "mmmm"
		, "mofo"
		, "niga"
		, "nigga"
		, "nigga"
		, "nigger"
		, "nigger"
		, "nnnn"
		, "nobhead"
		, "nonono"
		, "oooo"
		, "orgie"
		, "orgy"
		, "p33nis"
		, "p3nis"
		, "pedo"
		, "pen1s"
		, "penis"
		, "penis"
		, "poorrn"
		, "porn"
		, "porn"
		, "pppp"
		, "prick"
		, "prick"
		, "pussy"
		, "qqqq"
		, "re-tard"
		, "retard"
		, "rrrr"
		, "sex"
		, "sh1t"
		, "shi-"
		, "shiit"
		, "shit"
		, "shit"
		, "slag"
		, "slut"
		, "spanking"
		, "spunk"
		, "ssss"
		, "tits"
		, "tttt"
		, "twat"
		, "twat"
		, "uuuu"
		, "vagina"
		, "vagina"
		, "virgin"
		, "vvvv"
		, "wank"
		, "wank"
		, "whore"
		, "whore"
		, "wwww"
		, "xnxx"
		, "xxxx"
		, "yyyy"
		, "zzzz" };

	public static final String[] exceptionPhrases = { "wait wat",//
			"this hit",//
			"an album",//
			"finish it",//
			"minigame",//
			"just watching",//
			"s expensive",//
			"awesome",//
			"se xd",//
			"s except" //
	};

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);

	}

	public void onDisable() {

	}
}