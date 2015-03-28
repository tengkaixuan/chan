package com.dobi.jiecon.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class AppsNameFilter{
    public final static int MAX_APPNAME_LEN = 8;
	/**
	 * @return
	 */
	public static void matchFilter(LinkedList<IName> oriList, Set<String> excluded){
		for (Iterator<IName> iter=oriList.iterator(); iter.hasNext();)
		{
			if (excluded.contains(iter.next().getName())){
				iter.remove();
			}
		}	
	}
	public static void keywordsFilter(LinkedList<IName> oriList, Set<String> excludedKey){
		
		for (Iterator<IName> iter_ori=oriList.iterator(); iter_ori.hasNext();)
		{
			String cur_name = iter_ori.next().getName();
			
			for (Iterator<String> iter_ex = excludedKey.iterator(); iter_ex.hasNext();){
				if (cur_name.contains(iter_ex.next())){
					iter_ori.remove();
				}
			}
		}	
	}

    public static String appNameRefine(String appName){
        String name = appName;
        if (appName.length()>MAX_APPNAME_LEN){
            name = appName.substring(0, 6);
            name += "..";
        }

        return name;
    }
}
