package cn.forwode.tunnel;

import java.util.HashMap;
import java.util.Map;

public class ControlThreadManager{

    private static Map<String, Thread> threadsMap = new HashMap<String, Thread>();

    public static Thread getControlThread(String id){
        return threadsMap.get(id);
    }

    public static void addControlThread(String id, Thread controlThread){
        threadsMap.put(id, controlThread);
    }
}