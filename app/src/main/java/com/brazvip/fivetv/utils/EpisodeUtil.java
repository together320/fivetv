package com.brazvip.fivetv.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.brazvip.fivetv.beans.vod.VodChannelBean;


public class EpisodeUtil {
    public static int parseSeasonFromEpisode(VodChannelBean.Episode episode) {
        if (episode == null) {
            return 0;
        }
        int i = episode.season;
        if (i != 0) {
            return i;
        }
        String str = episode.title;
        if (str != null) {
            try {
                Matcher matcher = Pattern.compile("s(\\d+)[se]([\\w.]+)")
                        .matcher(str.toLowerCase(Locale.ROOT)
                                .replace(".", "")
                                .replace("-", "")
                                .replace(":", "")
                                .replace("=", "")
                                .replace("/", "")
                                .replace("|", ""));
                if (matcher.find()) {
                    return Integer.parseInt(matcher.group(1));
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }
}
